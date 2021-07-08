package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

public class UserDaoImpl implements UserDao {
    private static final String SELECT_ALL_USERS
            = "SELECT users.id, login, email, password_hash, salt, roles.name as role, statuses.name as status, first_name, " +
              "patronymic, last_name, profile_picture " +
              "FROM users " +
              "INNER JOIN roles " +
              "ON users.id_role = roles.id " +
              "INNER JOIN statuses " +
              "ON users.id_status = statuses.id;";

    private static final String SELECT_USER_BY_ID
            = "SELECT users.id, login, email, password_hash, salt, roles.name as role, statuses.name as status, first_name, " +
              "patronymic, last_name, profile_picture " +
              "FROM users " +
              "INNER JOIN roles " +
              "ON users.id_role = roles.id " +
              "INNER JOIN statuses " +
              "ON users.id_status = statuses.id " +
              "WHERE users.id = ?";

    private static final String SELECT_USER_BY_LOGIN
            = "SELECT users.id, login, email, password_hash, salt, roles.name as role, statuses.name as status, first_name, " +
              "patronymic, last_name, profile_picture " +
              "FROM users " +
              "INNER JOIN roles " +
              "ON users.id_role = roles.id " +
              "INNER JOIN statuses " +
              "ON users.id_status = statuses.id " +
              "WHERE login = ?";

    private static final String SELECT_USER_BY_LAST_NAME
            = "SELECT users.id, login, email, password_hash, salt, roles.name as role, statuses.name as status, first_name, " +
            "patronymic, last_name, profile_picture " +
            "FROM users " +
            "INNER JOIN roles " +
            "ON users.id_role = roles.id " +
            "INNER JOIN statuses " +
            "ON users.id_status = statuses.id " +
            "WHERE last_name = ?";

    private static final String INSERT_USER
            = "INSERT users (login, email, password_hash, salt, id_role, id_status, first_name, patronymic, last_name, " +
              "profile_picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_USER
            = "UPDATE users " +
              "SET login = ?, email = ?, password_hash = ?, salt = ?, id_role = ?, id_status = ?, first_name = ?, " +
              "patronymic = ?, last_name = ?, profile_picture = ? " +
              "WHERE id = ?";

    @Override
    public List<User> selectAll() throws DaoException {
        List<User> users = new LinkedList<>();
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS);

            while (resultSet.next()) {
                User user = extractUser(resultSet);
                users.add(user);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return users;
    }

    @Override
    public Optional<User> selectById(long id) throws DaoException {
        User user;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID);
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
    public void insert(User user) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER);
            setupPreparedStatement(statement, user);
            statement.execute();
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
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER);
            setupPreparedStatement(statement, user);
            statement.setLong(11, user.getEntityId());
            statement.execute();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> selectByLogin(String login) throws DaoException {
        User user;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
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
    public Optional<User> selectByLastName(String keyword) throws DaoException {
        User user;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_LAST_NAME);
            statement.setString(1, keyword);
            ResultSet resultSet = statement.executeQuery();
            user = resultSet.next() ? extractUser(resultSet) : null;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return user != null ? Optional.of(user) : Optional.empty();
    }

    private void setupPreparedStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPasswordHash());
        statement.setString(4, user.getSalt());
        statement.setLong(5, user.getUserRole().getRoleId());
        statement.setLong(6, user.getUserStatus().getStatusId());
        statement.setString(7, user.getFirstName());
        statement.setString(8, user.getPatronymic());
        statement.setString(9, user.getLastName());
        statement.setString(10, user.getProfilePicturePath());
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        return (User) User.builder()
                .setLogin(resultSet.getString(USER_LOGIN))
                .setEmail(resultSet.getString(USER_EMAIL))
                .setPasswordHash(resultSet.getString(USER_PASSWORD_HASH))
                .setSalt(resultSet.getString(USER_SALT))
                .setUserRole(UserRole.valueOf(resultSet.getString(USER_ROLE)))
                .setUserStatus(UserStatus.valueOf(resultSet.getString(USER_STATUS)))
                .setFirstName(resultSet.getString(USER_FIRST_NAME))
                .setPatronymic(resultSet.getString(USER_PATRONYMIC))
                .setLastName(resultSet.getString(USER_LAST_NAME))
                .setProfilePicturePath(resultSet.getString(USER_PROFILE_PICTURE))
                .setEntityId(resultSet.getLong(USER_ID))
                .build();
    }
}
