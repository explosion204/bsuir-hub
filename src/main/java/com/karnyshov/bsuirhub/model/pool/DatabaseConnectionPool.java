package com.karnyshov.bsuirhub.model.pool;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static AtomicReference<DatabaseConnectionPool> instance = new AtomicReference<>(null);
    private static final AtomicBoolean instanceInitialized = new AtomicBoolean(false);

    private static final String DB_PROPERTIES_NAME = "pool.properties";
    private static final String MIN_POOL_SIZE_PROPERTY = "poolMinSize";
    private static final String MAX_POOL_SIZE_PROPERTY = "poolMaxSize";
    private static final String POOL_VALIDATION_DELAY = "poolValidationDelay";
    private static final String POOL_VALIDATION_PERIOD = "poolValidationPeriod";
    private static final String CONNECTION_USAGE_TIMEOUT = "connectionUsageTimeout";

    private int poolMinSize;
    private int poolMaxSize;
    private int poolValidationDelay;
    private int poolValidationPeriod;
    private long connectionUsageTimeout;

    private BlockingDeque<Connection> availableConnections;
    private Deque<Pair<Connection, Instant>> busyConnections;

    // TODO: 6/13/2021 read about this mysterious field "fair"
    private final Lock availableConnectionsDequeLock = new ReentrantLock(true);
    private final Lock busyConnectionsDequeLock = new ReentrantLock(true);

    private DatabaseConnectionPool() {
        URL resource = getClass().getClassLoader().getResource(DB_PROPERTIES_NAME);

        if (resource == null) {
            logger.fatal("Unable to read database properties");
            throw new RuntimeException("Unable to read database properties");
        }

        String propertiesPath = new File(resource.getFile()).getAbsolutePath();

        try (InputStream inputStream = new FileInputStream(propertiesPath)) {
            Properties poolProperties = new Properties();
            poolProperties.load(inputStream);

            // configure pool size
            String poolMinSizePropertyValue = poolProperties.getProperty(MIN_POOL_SIZE_PROPERTY);
            String poolMaxSizePropertyValue = poolProperties.getProperty(MAX_POOL_SIZE_PROPERTY);
            poolMinSize = Integer.parseInt(poolMinSizePropertyValue);
            poolMaxSize = Integer.parseInt(poolMaxSizePropertyValue);

            if (poolMinSize > poolMaxSize) {
                logger.fatal("Min size of pool is greater than max size");
                throw new RuntimeException("Min size of pool is greater than max size ");
            }

            // configure pool validation parameters
            String poolValidationDelayPropertyValue = poolProperties.getProperty(POOL_VALIDATION_DELAY);
            String poolValidationPeriodPropertyValue = poolProperties.getProperty(POOL_VALIDATION_PERIOD);
            poolValidationDelay = Integer.parseInt(poolValidationDelayPropertyValue);
            poolValidationPeriod = Integer.parseInt(poolValidationPeriodPropertyValue);

            // configure connection usage timeout
            String connectionUsageTimeoutPropertyValue = poolProperties.getProperty(CONNECTION_USAGE_TIMEOUT);
            connectionUsageTimeout = Long.parseLong(connectionUsageTimeoutPropertyValue);

            // initialize pool
            availableConnections = new LinkedBlockingDeque<>(poolMaxSize);
            busyConnections = new ArrayDeque<>(poolMaxSize);

            for (int i = 0; i < poolMinSize; i++) {
                Connection connection = ConnectionFactory.createConnection();
                availableConnections.put(connection);
            }
        } catch (IOException e) {
            logger.fatal("Unable to read database properties", e);
            throw new RuntimeException("Unable to read database properties", e);
        } catch (NumberFormatException e) {
            logger.fatal("Unable to configure vital parameters of connection pool", e);
            throw new RuntimeException("Unable to configure size of connection pool", e);
        } catch (InterruptedException e) {
            logger.error("Caught an exception", e);
            Thread.currentThread().interrupt();
        } catch (DatabaseConnectionException e) {
            logger.error("Caught an error trying to establish connection", e);
        }

        if (availableConnections.size() < poolMinSize) {
            String message = "Unable to create pool due to lack of connections. Required " + poolMinSize + " but got "
                    + availableConnections.size();
            logger.fatal(message);
            throw new RuntimeException(message);
        }

        // schedule pool validation task
        Timer timer = new Timer(true);
        timer.schedule(new PoolValidationTask(), poolValidationDelay, poolValidationPeriod);
    }

    public static DatabaseConnectionPool getInstance() {
        while (instance.get() == null) {
            if (instanceInitialized.compareAndSet(false, true)) {
                instance.set(new DatabaseConnectionPool());
            }
        }

        return instance.get();
    }

    public Connection acquireConnection() throws DatabaseConnectionException {
        Connection connection = null;

        try {
            // establish new connection if pool is not full
            // otherwise wait for released connections
            connection = (availableConnections.size() < poolMaxSize)
                    ? ConnectionFactory.createConnection()
                    : availableConnections.take();

            Instant usageStart = Instant.now();

            busyConnectionsDequeLock.lock();
            busyConnections.offer(Pair.of(connection, usageStart));
        } catch (InterruptedException e) {
            logger.error("Caught an exception", e);
            Thread.currentThread().interrupt();
        } finally {
            busyConnectionsDequeLock.unlock();
        }

        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection.getClass() == ProxyConnection.class) {
            try {
                busyConnectionsDequeLock.lock();
                busyConnections.removeIf(pair -> pair.getLeft().equals(connection));
            } finally {
                busyConnectionsDequeLock.unlock();
            }

            try {
                availableConnectionsDequeLock.lock();
                availableConnections.put(connection);
            } catch (InterruptedException e) {
                logger.error("Caught an exception", e);
                Thread.currentThread().interrupt();
            } finally {
                availableConnectionsDequeLock.unlock();
            }
        } else {
            logger.warn("Trying to release a connection not supposed to be released!");
        }
    }

    public void destroyPool() {
        // this is action is terminating so finally block with guaranteed unlocks is not necessary
        availableConnectionsDequeLock.lock();
        busyConnectionsDequeLock.lock();

        for (int i = 0; i < poolMaxSize; i++) {
            try {
                ProxyConnection connection = (ProxyConnection) availableConnections.take();
                connection.closeWrappedConnection();
            } catch (InterruptedException e) {
                logger.error("Caught an exception", e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                logger.error("Unable to close connection in a proper way", e);
            }
        }

        DriverManager.getDrivers()
                .asIterator()
                .forEachRemaining(driver -> {
                    try {
                        DriverManager.deregisterDriver(driver);
                    } catch (SQLException e) {
                        logger.error("Failed to deregister driver: ", e);
                    }
                });

        availableConnectionsDequeLock.unlock();
        busyConnectionsDequeLock.unlock();
    }

    private class PoolValidationTask extends TimerTask {
        @Override
        public void run() {
            // close connections that are in use longer than permitted
            closeTimedOutConnections();

            // validate pool size
            validatePoolSize();

            // free pool decreasing amount of available connections to lower threshold by one every time task executed
            freePool();
        }

        private void closeTimedOutConnections() {
            for (Pair<Connection, Instant> pair : busyConnections) {
                ProxyConnection connection = (ProxyConnection) pair.getLeft();
                Instant usageStart = pair.getRight();
                Instant currentTimestamp = Instant.now();
                long usageDuration = Duration.between(usageStart, currentTimestamp).toMillis();

                if (usageDuration > connectionUsageTimeout) {
                    try {
                        // force leaked connection to be closed
                        busyConnectionsDequeLock.lock();
                        connection.closeWrappedConnection();
                        busyConnections.removeIf(p -> p.getLeft().equals(connection));
                    } catch (SQLException e) {
                        logger.error("Unable to close connection in a proper way", e);
                    } finally {
                        busyConnectionsDequeLock.unlock();
                    }
                }
            }
        }

        private void validatePoolSize() {
            availableConnectionsDequeLock.lock();
            busyConnectionsDequeLock.lock();
            try {
                while (availableConnections.size() + busyConnections.size() < poolMinSize) {
                    // add new available connections if a pool does not contain minimal required amount of connections
                    try {
                        Connection newConnection = ConnectionFactory.createConnection();
                        availableConnections.add(newConnection);
                    } catch (DatabaseConnectionException e) {
                        logger.error("Caught an error trying to establish connection", e);
                    }
                }
            } finally {
                availableConnectionsDequeLock.unlock();
                busyConnectionsDequeLock.unlock();
            }
        }

        private void freePool() {
            availableConnectionsDequeLock.lock();
            busyConnectionsDequeLock.lock();
            try {
                if (availableConnections.size() + busyConnections.size() > poolMinSize) {
                    ProxyConnection connection = (ProxyConnection) availableConnections.poll();

                    if (connection != null) {
                        try {
                            connection.closeWrappedConnection();
                        } catch (SQLException e) {
                            logger.error("Unable to close connection in a proper way", e);
                        }
                    }
                }
            } finally {
                availableConnectionsDequeLock.unlock();
                busyConnectionsDequeLock.unlock();
            }
        }
    }
}
