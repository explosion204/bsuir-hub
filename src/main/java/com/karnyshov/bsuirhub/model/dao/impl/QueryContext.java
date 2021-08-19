package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;


/**
 * {@code QueryContext} class encapsulates logic for querying database. It also allows to execute sequence of queries
 * as an atomic action (transaction).
 * @author Dmitry Karnyshov
 */
class QueryContext {
    private static final Logger logger = LogManager.getLogger();

    private Connection contextConnection;
    private boolean isTransaction;
    private boolean isTerminated;

    private QueryContext(boolean isTransaction) throws DatabaseConnectionException, SQLException {
        this.isTransaction = isTransaction;
        contextConnection = DatabaseConnectionPool.getInstance().acquireConnection();

        if (isTransaction) {
            contextConnection.setAutoCommit(false);
        }
    }

    /**
     * Creates context instance.
     *
     * @param isTransaction {@code true} value allows context execute queries as transaction,
     *      {@code false} allows executing the only request with subsequent context termination.
     * @return context instance.
     * @throws DaoException if an error occurred while creating context.
     */
    static QueryContext createContext(boolean isTransaction) throws DaoException {
        try {
            return new QueryContext(isTransaction);
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Execute SELECT query.
     *
     * @param <T> type of objects in the result list.
     * @param mapper mapper for {@code T}.
     * @param sqlQuery SQL query string which will be executed as prepared statement.
     * @param result {@link List} instance to hold objects retrieved from database.
     * @param params parameters that will be set to prepared statement.
     * @throws DaoException if an error occurred while processing the query.
     */
    <T> void executeSelect(ResultSetMapper<T> mapper, String sqlQuery, List<T> result, Object ... params)
            throws DaoException {
        if (isTerminated) {
            throw new DaoException("Context is already terminated");
        }

        try (PreparedStatement statement = contextConnection.prepareStatement(sqlQuery)) {
            initPreparedStatement(statement, params);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    T mappedObject = mapper.map(resultSet);
                    result.add(mappedObject);
                }
            }
        } catch (SQLException e) {
            terminateContext();
            throw new DaoException(e);
        } finally {
            if (!isTransaction && !isTerminated) {
                terminateContext();
            }
        }
    }

    /**
     * Execute SELECT query for single result.
     *
     * @param <T> type of object retrieved from database.
     * @param mapper mapper for {@code T}.
     * @param sqlQuery SQL query string which will be executed as prepared statement.
     * @param params parameters that will be set to prepared statement.
     * @return {@code T} object wrapped with {@link Optional}.
     * @throws DaoException if an error occurred while processing the query.
     */
    <T> Optional<T> executeSelectForSingleResult(ResultSetMapper<T> mapper, String sqlQuery, Object ... params)
                throws DaoException {
        if (isTerminated) {
            throw new DaoException("Context is already terminated");
        }

        T mappedObject = null;

        try (PreparedStatement statement = contextConnection.prepareStatement(sqlQuery)) {
            initPreparedStatement(statement, params);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    mappedObject = mapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            terminateContext();
            throw new DaoException(e);
        } finally {
            if (!isTransaction && !isTerminated) {
                terminateContext();
            }
        }

        return mappedObject != null ? Optional.of(mappedObject) : Optional.empty();
    }

    /**
     * Execute INSERT long.
     *
     * @param sqlQuery SQL query string which will be executed as prepared statement.
     * @param params parameters that will be set to prepared statement.
     * @return {@code long} value that represents generated id of freshly-inserted object.
     * @throws DaoException if an error occurred while processing the query.
     */
    long executeInsert(String sqlQuery, Object ... params) throws DaoException {
        if (isTerminated) {
            throw new DaoException("Context is already terminated");
        }

        try (PreparedStatement statement = contextConnection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            initPreparedStatement(statement, params);
            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                return generatedKeys.next() ? generatedKeys.getLong(1) : 0;
            }
        } catch (SQLException e) {
            if (isTransaction) {
                try {
                    contextConnection.rollback();
                } catch (SQLException ex) {
                    throw new DaoException(ex);
                }
            }

            terminateContext();
            throw new DaoException(e);
        } finally {
            if (!isTransaction && !isTerminated) {
                terminateContext();
            }
        }
    }

    /**
     * Execute UPDATE or DELETE queries.
     *
     * @param sqlQuery SQL query string which will be executed as prepared statement.
     * @param params parameters that will be set to prepared statement.
     * @throws DaoException if an error occurred while processing the query.
     */
    int executeUpdateOrDelete(String sqlQuery, Object ... params) throws DaoException {
        if (isTerminated) {
            throw new DaoException("Context is already terminated");
        }

        try (PreparedStatement statement = contextConnection.prepareStatement(sqlQuery)) {
            initPreparedStatement(statement, params);
            return statement.executeUpdate();
        } catch (SQLException e) {
            if (isTransaction) {
                try {
                    contextConnection.rollback();
                } catch (SQLException ex) {
                    throw new DaoException(ex);
                }
            }

            terminateContext();
            throw new DaoException(e);
        } finally {
            if (!isTransaction && !isTerminated) {
                terminateContext();
            }
        }
    }

    /**
     * Commit transaction.
     *
     * @throws DaoException if a database error occurred.
     */
    void commit() throws DaoException {
        if (isTerminated) {
            throw new DaoException("Context is already terminated");
        }

        if (isTransaction) {
            try {
                contextConnection.commit();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }

        terminateContext();
    }

    private void terminateContext() {
        isTerminated = true;

        try {
            if (isTransaction) {
                contextConnection.setAutoCommit(true);
            }

            contextConnection.close();
        } catch (SQLException e) {
            logger.error("An error occurred trying to close connection: ", e);
        }
    }

    private void initPreparedStatement(PreparedStatement statement, Object ... params) throws SQLException {
        int i = 1;

        for (Object parameter : params) {
            if (parameter != null) {
                statement.setObject(i++, parameter);
            } else {
                statement.setNull(i++, Types.NULL);
            }
        }
    }
}
