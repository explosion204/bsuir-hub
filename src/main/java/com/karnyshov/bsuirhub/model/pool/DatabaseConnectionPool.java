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
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    private BlockingDeque<Pair<Connection, Instant>> busyConnections;
    private final Timer timer = new Timer(true);

    private DatabaseConnectionPool() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(DB_PROPERTIES_NAME)) {
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
            busyConnections = new LinkedBlockingDeque<>(poolMaxSize);

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
        PoolValidationTask task = new PoolValidationTask(connectionUsageTimeout, availableConnections, busyConnections,
                poolMinSize);
        timer.schedule(task, poolValidationDelay, poolValidationPeriod);
    }

    public static DatabaseConnectionPool getInstance() {
        while (instance.get() == null) {
            if (instanceInitialized.compareAndSet(false, true)) {
                instance.set(new DatabaseConnectionPool());
                System.out.println("hello");
            }
        }

        return instance.get();
    }


    public Connection acquireConnection() throws DatabaseConnectionException {
        Connection connection = null;

        try {
            // establish new connection if pool is not full
            // otherwise wait for released connections
            connection = (availableConnections.size() + busyConnections.size() < poolMaxSize)
                    ? ConnectionFactory.createConnection()
                    : availableConnections.take();

            Instant usageStart = Instant.now();
            busyConnections.put(Pair.of(connection, usageStart));
        } catch (InterruptedException e) {
            logger.error("Caught an exception", e);
            Thread.currentThread().interrupt();
        }

        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection.getClass() == ProxyConnection.class) {
            busyConnections.removeIf(pair -> pair.getLeft().equals(connection));

            try {
                availableConnections.put(connection);
            } catch (InterruptedException e) {
                logger.error("Caught an exception", e);
                Thread.currentThread().interrupt();
            }
        } else {
            logger.warn("Trying to release a connection not supposed to be released!");
        }
    }

    public void destroyPool() {
        timer.cancel(); // cancel validation task

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
    }
}
