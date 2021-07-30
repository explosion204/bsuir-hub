package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import jakarta.inject.Named;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class FacultyDaoImpl implements FacultyDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name " +
              "FROM faculties " +
              "WHERE is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM faculties " +
              "WHERE is_archived = 0;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, short_name " +
              "FROM faculties " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, short_name " +
              "FROM faculties " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM faculties " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String SELECT_BY_SHORT_NAME
            = "SELECT id, name, short_name " +
            "FROM faculties " +
            "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
            "ORDER BY id " +
            "LIMIT ? " +
            "OFFSET ?;";

    private static final String SELECT_COUNT_BY_SHORT_NAME
            = "SELECT COUNT(id) " +
              "FROM faculties " +
              "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String INSERT
            = "INSERT faculties (name, short_name) VALUES (?, ?);";

    private static final String UPDATE
            = "UPDATE faculties " +
              "SET name = ?, short_name = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE faculties " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    @Override
    public void selectAll(int offset, int limit, List<Faculty> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Faculty faculty = extractFaculty(resultSet);
                result.add(faculty);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public long selectTotalCount() throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        long totalCount;

        try {
            connection = pool.acquireConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_TOTAL_COUNT);
            resultSet.next();
            totalCount = resultSet.getLong(1);
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return totalCount;
    }

    @Override
    public Optional<Faculty> selectById(long id) throws DaoException {
        Faculty faculty;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            faculty = resultSet.next() ? extractFaculty(resultSet) : null;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return faculty != null ? Optional.of(faculty) : Optional.empty();
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME);
            statement.setString(1, keyword);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Faculty faculty = extractFaculty(resultSet);
                result.add(faculty);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        long result;

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_BY_NAME);
            statement.setString(1, keyword);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getLong(1);
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return result;
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME);
            statement.setString(1, keyword);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Faculty faculty = extractFaculty(resultSet);
                result.add(faculty);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public long selectCountByShortName(String keyword) {
        return 0; // TODO: 7/29/2021
    }

    @Override
    public long insert(Faculty entity) throws DaoException {
        return 0; // TODO: 7/29/2021
    }

    @Override
    public void update(Faculty entity) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/29/2021
    }

    private Faculty extractFaculty(ResultSet resultSet) throws SQLException {
        return (Faculty) Faculty.builder()
                .setName(resultSet.getString(FACULTY_NAME))
                .setShortName(resultSet.getString(FACULTY_SHORT_NAME))
                .setArchived(resultSet.getBoolean(FACULTY_IS_ARCHIVED))
                .setEntityId(resultSet.getLong(FACULTY_ID))
                .build();
    }
}

