package com.karnyshov.bsuirhub.model.pool;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static DatabaseConnectionPool instance;
    private static final AtomicBoolean instanceInitialized = new AtomicBoolean(false);

    private static final String DB_PROPERTIES_NAME = "db.properties";
    private static final String DB_DRIVER_NAME_PROPERTY = "driver";
    private static final String DB_URL_PROPERTY = "url";
    private static final String DB_MIN_POOL_SIZE_PROPERTY = "poolMinSize";
    private static final String DB_MAX_POOL_SIZE_PROPERTY = "poolMaxSize";
    private static final String DB_POOL_VALIDATION_DELAY = "poolValidationDelay";
    private static final String DB_POOL_VALIDATION_PERIOD = "poolValidationPeriod";
    private static final String DB_CONNECTION_USAGE_TIMEOUT = "connectionUsageTimeout";

    private String dbUrl;
    private int poolMinSize;
    private int poolMaxSize;
    private int poolValidationDelay;
    private int poolValidationPeriod;
    private int connectionUsageTimeout;

    private BlockingDeque<Connection> availableConnections;
    private BlockingDeque<Pair<Connection, Instant>> busyConnections;

    private DatabaseConnectionPool() {
        URL resource = getClass().getClassLoader().getResource(DB_PROPERTIES_NAME);

        if (resource == null) {
            logger.fatal("Unable to read database properties");
            throw new RuntimeException("Unable to read database properties");
        }

        String propertiesPath = new File(resource.getFile()).getAbsolutePath();

        try (InputStream inputStream = new FileInputStream(propertiesPath)) {
            Properties dbProperties = new Properties();
            dbProperties.load(inputStream);

            // register driver
            String driver = dbProperties.getProperty(DB_DRIVER_NAME_PROPERTY);
            Class.forName(driver);

            // retrieve database URL
            dbUrl = dbProperties.getProperty(DB_URL_PROPERTY);

            // configure pool size
            String poolMinSizePropertyValue = dbProperties.getProperty(DB_MIN_POOL_SIZE_PROPERTY);
            String poolMaxSizePropertyValue = dbProperties.getProperty(DB_MAX_POOL_SIZE_PROPERTY);
            poolMinSize = Integer.parseInt(poolMinSizePropertyValue);
            poolMaxSize = Integer.parseInt(poolMaxSizePropertyValue);

            if (poolMinSize > poolMaxSize) {
                logger.fatal("Min size of pool is greater than max size of pool");
                throw new RuntimeException("Min size of pool is greater than max size of pool");
            }

            // configure pool validation parameters
            String poolValidationDelayPropertyValue = dbProperties.getProperty(DB_POOL_VALIDATION_DELAY);
            String poolValidationPeriodPropertyValue = dbProperties.getProperty(DB_POOL_VALIDATION_PERIOD);
            poolValidationDelay = Integer.parseInt(poolValidationDelayPropertyValue);
            poolValidationPeriod = Integer.parseInt(poolValidationPeriodPropertyValue);

            // configure connection use timeout
            String connectionUsageTimeoutPropertyValue = dbProperties.getProperty(DB_CONNECTION_USAGE_TIMEOUT);
            connectionUsageTimeout = Integer.parseInt(connectionUsageTimeoutPropertyValue);

            // initialize pool
            availableConnections = new LinkedBlockingDeque<>(poolMaxSize);
            busyConnections = new LinkedBlockingDeque<>(poolMaxSize);

            for (int i = 0; i < poolMinSize; i++) {
                Connection connection = DriverManager.getConnection(dbUrl, dbProperties);
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                availableConnections.put(proxyConnection);
            }
        } catch (IOException e) {
            logger.fatal("Unable to read database properties", e);
            throw new RuntimeException("Unable to read database properties", e);
        } catch (ClassNotFoundException e) {
            logger.fatal("Cannot load specified database driver", e);
            throw new RuntimeException("Cannot load specified database driver", e);
        } catch (NumberFormatException e) {
            logger.fatal("Unable to configure vital parameters of connection pool", e);
            throw new RuntimeException("Unable to configure size of connection pool", e);
        } catch (SQLException e) {
            logger.error("Unable to establish connection", e);
        } catch (InterruptedException e) {
            logger.error("Caught an exception", e);
            Thread.currentThread().interrupt();
        }

        if (availableConnections.size() < poolMinSize) {
            String message = "Unable to create pool due to lack of connections. Required " + poolMinSize + " but got "
                    + availableConnections.size();
            logger.fatal(message);
            throw new RuntimeException(message);
        }

        Timer timer = new Timer(true);
        timer.schedule(new PoolValidationTask(), poolValidationDelay, poolValidationPeriod);
    }

    public static DatabaseConnectionPool getInstance() {
        while (instance == null) {
            if (instanceInitialized.compareAndSet(false, true)) {
                instance = new DatabaseConnectionPool();
            }
        }

        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            connection = availableConnections.take();
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
            busyConnections.remove(connection);

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
    }

    private void deregisterDrivers() {
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

    private class PoolValidationTask extends TimerTask {
        @Override
        public void run() {

        }
    }
}
