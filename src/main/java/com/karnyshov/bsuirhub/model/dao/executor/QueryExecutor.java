package com.karnyshov.bsuirhub.model.dao.executor;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * {@code QueryExecutor} class provides a bunch of generic static methods to query database.
 */
public class QueryExecutor {
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
    public static <T> void executeSelect(ResultSetMapper<T> mapper, String sqlQuery, List<T> result, Object ... params)
                throws DaoException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try (Connection connection = pool.acquireConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            initPreparedStatement(statement, params);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    T mappedObject = mapper.map(resultSet);
                    result.add(mappedObject);
                }
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
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
    public static <T> Optional<T> executeSelectForSingleResult(ResultSetMapper<T> mapper, String sqlQuery,
                Object ... params) throws DaoException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        T mappedObject = null;

        try (Connection connection = pool.acquireConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            initPreparedStatement(statement, params);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    mappedObject = mapper.map(resultSet);
                }
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
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
    public static long executeInsert(String sqlQuery, Object ... params) throws DaoException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try (Connection connection = pool.acquireConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            initPreparedStatement(statement, params);
            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                return generatedKeys.next() ? generatedKeys.getLong(1) : 0;
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Execute UPDATE or DELETE queries.
     *
     * @param sqlQuery SQL query string which will be executed as prepared statement.
     * @param params parameters that will be set to prepared statement.
     * @throws DaoException if an error occurred while processing the query.
     */
    public static int executeUpdateOrDelete(String sqlQuery, Object ... params) throws DaoException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try (Connection connection = pool.acquireConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            initPreparedStatement(statement, params);
            return statement.executeUpdate();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    private static void initPreparedStatement(PreparedStatement statement, Object ... params) throws SQLException {
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
