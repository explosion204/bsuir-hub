package com.karnyshov.bsuirhub.model.pool;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code DatabaseConnectionPool} class represents thread-safe object pool which contains database connections.
 * @author Dmitry Karnyshov
 */
public class DatabaseConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static AtomicReference<DatabaseConnectionPool> instance = new AtomicReference<>(null);
    private static AtomicBoolean instanceInitialized = new AtomicBoolean(false);

    private static final String DB_PROPERTIES_NAME = "pool.properties";
    private static final String MIN_POOL_SIZE_PROPERTY = "pool_min_size";
    private static final String MAX_POOL_SIZE_PROPERTY = "pool_max_size";
    private static final String POOL_VALIDATION_DELAY = "pool_validation_delay";
    private static final String POOL_VALIDATION_PERIOD = "pool_validation_period";
    private static final String CONNECTION_USAGE_TIMEOUT = "connection_usage_timeout";

    private int poolMinSize;
    private int poolMaxSize;
    private int poolValidationDelay;
    private int poolValidationPeriod;
    private long connectionUsageTimeout;

    private Lock connectionPoolLock = new ReentrantLock(true);
    private Condition hasAvailableConnections = connectionPoolLock.newCondition();
    private Timer timer = new Timer(true);
    private Queue<Connection> availableConnections;
    private Queue<Pair<Connection, Instant>> busyConnections;

    private DatabaseConnectionPool() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(DB_PROPERTIES_NAME)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            // configure pool size
            poolMinSize = Integer.parseInt(properties.getProperty(MIN_POOL_SIZE_PROPERTY));
            poolMaxSize = Integer.parseInt(properties.getProperty(MAX_POOL_SIZE_PROPERTY));

            if (poolMinSize > poolMaxSize) {
                logger.fatal("Min size of pool is greater than max size");
                throw new RuntimeException("Min size of pool is greater than max size");
            }

            // configure pool validation parameters
            poolValidationDelay = Integer.parseInt(properties.getProperty(POOL_VALIDATION_DELAY));
            poolValidationPeriod = Integer.parseInt(properties.getProperty(POOL_VALIDATION_PERIOD));

            // configure connection usage timeout
            connectionUsageTimeout = Long.parseLong(properties.getProperty(CONNECTION_USAGE_TIMEOUT));

            // initialize pool
            availableConnections = new ArrayDeque<>(poolMaxSize);
            busyConnections = new ArrayDeque<>(poolMaxSize);

            for (int i = 0; i < poolMinSize; i++) {
                Connection connection = ConnectionFactory.createConnection();
                availableConnections.add(connection);
            }
        } catch (IOException e) {
            logger.fatal("Unable to read database properties", e);
            throw new RuntimeException("Unable to read database properties", e);
        } catch (NumberFormatException e) {
            logger.fatal("Unable to configure vital parameters of connection pool", e);
            throw new RuntimeException("Unable to configure size of connection pool", e);
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
        PoolValidationTask task = new PoolValidationTask();
        timer.schedule(task, poolValidationDelay, poolValidationPeriod);
    }

    /**
     * Get instance of {@code DatabaseConnectionPool} class.
     *
     * @return {@code DatabaseConnectionPool} instance.
     */
    public static DatabaseConnectionPool getInstance() {
        while (instance.get() == null) {
            if (instanceInitialized.compareAndSet(false, true)) {
                instance.set(new DatabaseConnectionPool());
            }
        }

        return instance.get();
    }


    /**
     * Acquire a database connection. This method is blocking, callee thread has to wait until pool has available
     * connections.
     *
     * @return {@code Connection} instance.
     * @throws DatabaseConnectionException if application is unable to establish proper connection.
     */
    public Connection acquireConnection() throws DatabaseConnectionException {
        Connection connection = null;

        try {
            connectionPoolLock.lock();

            if (availableConnections.size() + busyConnections.size() < poolMaxSize) {
                connection = ConnectionFactory.createConnection();
            } else {
                if (availableConnections.isEmpty()) {
                    hasAvailableConnections.await();
                }

                connection = availableConnections.remove();
            }

            Instant usageStart = Instant.now();
            busyConnections.add(Pair.of(connection, usageStart));
        } catch (InterruptedException e) {
            logger.error("Caught an exception", e);
            Thread.currentThread().interrupt();
        } finally {
            connectionPoolLock.unlock();
        }

        return connection;
    }

    /**
     * Release connection and return it to the pool.
     *
     * @param connection {@code Connection} instance that must be returned to the pool.
     */
    public boolean releaseConnection(Connection connection) {
        boolean result;
        if (connection != null && connection.getClass() == ProxyConnection.class) {
            try {
                connectionPoolLock.lock();
                busyConnections.removeIf(pair -> pair.getLeft().equals(connection));
                availableConnections.add(connection);
            } finally {
                hasAvailableConnections.signal();
                connectionPoolLock.unlock();
            }

            result = true;
        } else {
            result = false;
            logger.warn("Trying to release a connection not supposed to be released!");
        }

        return result;
    }

    /**
     * Destroy pool and free all resources.
     */
    public void destroyPool() {
        timer.cancel(); // cancel validation task
        connectionPoolLock.lock();
        int activeConnections = availableConnections.size() + busyConnections.size();

        for (int i = 0; i < activeConnections; i++) {
            try {
                if (availableConnections.isEmpty()) {
                    hasAvailableConnections.await();
                }

                ProxyConnection connection = (ProxyConnection) availableConnections.remove();
                connection.closeWrappedConnection();
            } catch (InterruptedException e) {
                logger.error("Caught an exception", e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                logger.error("Unable to close connection in a proper way", e);
            }
        }

        connectionPoolLock.unlock();
        DriverManager.getDrivers()
                .asIterator()
                .forEachRemaining(driver -> {
                    try {
                        DriverManager.deregisterDriver(driver);
                    } catch (SQLException e) {
                        logger.error("Failed to deregister driver: ", e);
                    }
                });
    }


    /* package-private methods used for pool validation */
    Lock getConnectionPoolLock() {
        return connectionPoolLock;
    }
    Queue<Connection> getAvailableConnections() {
        return availableConnections;
    }
    Queue<Pair<Connection, Instant>> getBusyConnections() {
        return busyConnections;
    }
    long getConnectionUsageTimeout() {
        return connectionUsageTimeout;
    }
    int getPoolMinSize() {
        return poolMinSize;
    }
}
