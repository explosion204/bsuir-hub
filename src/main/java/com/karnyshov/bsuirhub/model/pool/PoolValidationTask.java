package com.karnyshov.bsuirhub.model.pool;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;

class PoolValidationTask extends TimerTask {
    private static final Logger logger = LogManager.getLogger();
    private final long connectionUsageTimeout;
    private final int poolMinSize;
    private final BlockingDeque<Connection> availableConnections;
    private final BlockingDeque<Pair<Connection, Instant>> busyConnections;

    PoolValidationTask(long connectionUsageTimeout, BlockingDeque<Connection> availableConnections,
                BlockingDeque<Pair<Connection, Instant>> busyConnections, int poolMinSize) {
        this.connectionUsageTimeout = connectionUsageTimeout;
        this.availableConnections = availableConnections;
        this.busyConnections = busyConnections;
        this.poolMinSize = poolMinSize;
    }

    @Override
    public void run() {
        // validate pool size
        validatePoolSize();

        // close connections that are in use longer than permitted
        closeTimedOutConnections();

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
                    connection.closeWrappedConnection();
                    busyConnections.removeIf(p -> p.getLeft().equals(connection));

                    Connection newConnection = ConnectionFactory.createConnection();
                    availableConnections.offer(newConnection);
                } catch (SQLException e) {
                    logger.error("Unable to close connection in a proper way", e);
                } catch (DatabaseConnectionException e) {
                    logger.error("Caught an error trying to establish connection", e);
                }
            }
        }
    }

    private void validatePoolSize() {
        while (availableConnections.size() + busyConnections.size() < poolMinSize) {
            // add new available connections if a pool does not contain minimal required amount of connections
            try {
                Connection newConnection = ConnectionFactory.createConnection();
                availableConnections.offer(newConnection);
            } catch (DatabaseConnectionException e) {
                logger.error("Caught an error trying to establish connection", e);
            }
        }
    }

    private void freePool() {
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
    }
}
