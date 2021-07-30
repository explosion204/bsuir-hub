package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import jakarta.inject.Named;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class UserDaoImpl implements UserDao { // TODO: 7/23/2021 boilerplate ?
    private static final String SELECT_ALL
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE id_status <> 3;";

    private static final String SELECT_BY_ID
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE users.id = ?;";

    private static final String SELECT_BY_LOGIN
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE login = ? AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_LOGIN
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE login LIKE CONCAT('%', ?, '%') AND id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_LOGIN
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE login LIKE CONCAT('%', ?, '%') AND id_status <> 3;";

    private static final String SELECT_BY_EMAIL
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE email = ? AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_EMAIL
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE email LIKE CONCAT('%', ?, '%') AND id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_EMAIL
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE email LIKE CONCAT('%', ?, '%') AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_LAST_NAME
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_picture " +
              "FROM users " +
              "WHERE last_name LIKE CONCAT('%', ?, '%') AND id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_LAST_NAME
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE last_name LIKE CONCAT('%', ?, '%') AND id_status <> 3;";

    private static final String INSERT
            = "INSERT users (login, email, password_hash, salt, id_role, id_status, id_group, first_name, patronymic, " +
              "last_name, profile_picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE users " +
              "SET email = ?, password_hash = ?, salt = ?, id_role = ?, id_status = ?, id_group = ?, first_name = ?, " +
              "patronymic = ?, last_name = ?, profile_picture = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE users " +
              "SET id_status = 3 " +
              "WHERE id = ?;";

    @Override
    public void selectAll(int offset, int limit, List<User> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = extractUser(resultSet);
                result.add(user);
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
    public Optional<User> selectById(long id) throws DaoException {
        User user;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            user = resultSet.next() ? extractUser(resultSet) : null;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> selectByLogin(String login) throws DaoException {
        User user;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            user = resultSet.next() ? extractUser(resultSet) : null;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public void selectByLogin(int offset, int limit, String keyword, List<User> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_MULTIPLE_BY_LOGIN);
            statement.setString(1, keyword);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = extractUser(resultSet);
                result.add(user);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public long selectCountByLogin(String keyword) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        long result;

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_BY_LOGIN);
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
    public Optional<User> selectByEmail(String email) throws DaoException {
        User user;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            user = resultSet.next() ? extractUser(resultSet) : null;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public void selectByEmail(int offset, int limit, String keyword, List<User> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_MULTIPLE_BY_EMAIL);
            statement.setString(1, keyword);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = extractUser(resultSet);
                result.add(user);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public long selectCountByEmail(String keyword) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        long result;

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_BY_EMAIL);
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
    public void selectByLastName(int offset, int limit, String keyword, List<User> result) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_MULTIPLE_BY_LAST_NAME);
            statement.setString(1, keyword);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = extractUser(resultSet);
                result.add(user);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public long selectCountByLastName(String keyword) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        long result;

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_BY_LAST_NAME);
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
    public long insert(User user) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();


        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getSalt());
            statement.setLong(5, user.getUserRole().getRoleId());
            statement.setLong(6, user.getUserStatus().getStatusId());
            statement.setLong(7, user.getGroupId());
            statement.setString(8, user.getFirstName());
            statement.setString(9, user.getPatronymic());
            statement.setString(10, user.getLastName());
            statement.setString(11, user.getProfilePicturePath());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            return generatedKeys.next() ? generatedKeys.getLong(1) : 0;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public void update(User user) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getSalt());
            statement.setLong(4, user.getUserRole().getRoleId());
            statement.setLong(5, user.getUserStatus().getStatusId());
            statement.setLong(6, user.getGroupId());
            statement.setString(7, user.getFirstName());
            statement.setString(8, user.getPatronymic());
            statement.setString(9, user.getLastName());
            statement.setString(10, user.getProfilePicturePath());
            statement.setLong(11, user.getEntityId());
            statement.execute();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.execute();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        return (User) User.builder()
                .setLogin(resultSet.getString(USER_LOGIN))
                .setEmail(resultSet.getString(USER_EMAIL))
                .setPasswordHash(resultSet.getString(USER_PASSWORD_HASH))
                .setSalt(resultSet.getString(USER_SALT))
                .setUserRole(UserRole.parseRole(resultSet.getLong(USER_ROLE_ID)))
                .setUserStatus(UserStatus.parseStatus(resultSet.getLong(USER_STATUS_ID)))
                .setGroupId(resultSet.getLong(USER_GROUP_ID))
                .setFirstName(resultSet.getString(USER_FIRST_NAME))
                .setPatronymic(resultSet.getString(USER_PATRONYMIC))
                .setLastName(resultSet.getString(USER_LAST_NAME))
                .setProfilePicturePath(resultSet.getString(USER_PROFILE_PICTURE))
                .setEntityId(resultSet.getLong(USER_ID))
                .build();
    }
}
