package com.karnyshov.bsuirhub.model.dao.executor;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class QueryExecutor {
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

    public static void executeUpdateOrDelete(String sqlQuery, Object ... params) throws DaoException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try (Connection connection = pool.acquireConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            initPreparedStatement(statement, params);
            statement.execute();
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
