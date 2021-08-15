package com.karnyshov.bsuirhub.model.pool;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

/**
 * {@code PoolValidationTask} is a subclass of {@link TimerTask} and used for pool integrity validation purposes.
 * @author Dmitry Karnyshov
 */
class PoolValidationTask extends TimerTask {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run() {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        Lock connectionPoolLock = pool.getConnectionPoolLock();
        Queue<Connection> availableConnections = pool.getAvailableConnections();
        Queue<Pair<Connection, Instant>> busyConnections = pool.getBusyConnections();
        long connectionUsageTimeout = pool.getConnectionUsageTimeout();
        int poolMinSize = pool.getPoolMinSize();

        try {
            connectionPoolLock.lock();

            // close connections that are in use longer than permitted
            closeTimedOutConnections(busyConnections, connectionUsageTimeout);

            // validate pool size
            validatePoolSize(availableConnections, busyConnections, poolMinSize);

            // free pool decreasing amount of available connections to lower threshold by one every time task executed
            freePool(availableConnections, busyConnections, poolMinSize);
        } finally {
            connectionPoolLock.unlock();
        }
    }

    private void closeTimedOutConnections(Queue<Pair<Connection, Instant>> busyConnections, long connectionUsageTimeout) {
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
                } catch (SQLException e) {
                    logger.error("Unable to close connection in a proper way", e);
                }
            }
        }
    }

    private void validatePoolSize(Queue<Connection> availableConnections,
                Queue<Pair<Connection, Instant>> busyConnections, int poolMinSize) {
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

    private void freePool(Queue<Connection> availableConnections,
                Queue<Pair<Connection, Instant>> busyConnections, int poolMinSize) {

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
