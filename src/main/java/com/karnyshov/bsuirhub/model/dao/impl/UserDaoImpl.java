package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import jakarta.inject.Inject;
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

    @Inject
    private ResultSetMapper<User> userMapper;

    @Inject
    private ResultSetMapper<Long> longMapper;

    @Override
    public void selectAll(int offset, int limit, List<User> result) throws DaoException {
        QueryExecutor.executeSelect(userMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public long selectTotalCount() throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<User> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(userMapper, SELECT_BY_ID, id);
    }

    @Override
    public Optional<User> selectByLogin(String login) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(userMapper, SELECT_BY_LOGIN, login);
    }

    @Override
    public void selectByLogin(int offset, int limit, String keyword, List<User> result) throws DaoException {
        QueryExecutor.executeSelect(userMapper, SELECT_MULTIPLE_BY_LOGIN, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByLogin(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_LOGIN, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_LOGIN query"));
    }

    @Override
    public Optional<User> selectByEmail(String email) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(userMapper, SELECT_BY_EMAIL, email);
    }

    @Override
    public void selectByEmail(int offset, int limit, String keyword, List<User> result) throws DaoException {
        QueryExecutor.executeSelect(userMapper, SELECT_MULTIPLE_BY_EMAIL, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByEmail(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_EMAIL, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_EMAIL query"));
    }

    @Override
    public void selectByLastName(int offset, int limit, String keyword, List<User> result) throws DaoException {
        QueryExecutor.executeSelect(userMapper, SELECT_MULTIPLE_BY_LAST_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByLastName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_LAST_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_LAST_NAME query"));
    }

    @Override
    public long insert(User user) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                user.getLogin(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getSalt(),
                user.getUserRole().getRoleId(),
                user.getUserStatus().getStatusId(),
                user.getGroupId(),
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getProfilePicturePath()
        );
    }

    @Override
    public void update(User user) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                user.getEmail(),
                user.getPasswordHash(),
                user.getSalt(),
                user.getUserRole().getRoleId(),
                user.getUserStatus().getStatusId(),
                user.getGroupId(),
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getProfilePicturePath(),
                user.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
