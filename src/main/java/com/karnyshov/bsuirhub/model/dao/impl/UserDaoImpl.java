package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.SubjectDao;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.User;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

/**
 * {@code UserDaoImpl} is an implementation of {@link UserDao} interfaces.
 * @author Dmitry Karnyshov
 */
@Named
public class UserDaoImpl implements UserDao {
    private static final String SELECT_ALL
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_image " +
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
              "id_group, first_name, patronymic, last_name, profile_image " +
              "FROM users " +
              "WHERE users.id = ?;";

    private static final String SELECT_BY_LOGIN
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_image " +
              "FROM users " +
              "WHERE login = ? AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_LOGIN
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_image " +
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
              "id_group, first_name, patronymic, last_name, profile_image " +
              "FROM users " +
              "WHERE email = ? AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_EMAIL
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_image " +
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
              "id_group, first_name, patronymic, last_name, profile_image " +
              "FROM users " +
              "WHERE last_name LIKE CONCAT('%', ?, '%') AND id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_LAST_NAME
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE last_name LIKE CONCAT('%', ?, '%') AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_ROLE
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_image " +
              "FROM users " +
              "WHERE id_role = ? AND id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_ROLE
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE id_role = ? AND id_status <> 3;";

    private static final String SELECT_MULTIPLE_BY_GROUP
            = "SELECT id, login, email, password_hash, salt, id_role, id_status, " +
              "id_group, first_name, patronymic, last_name, profile_image " +
              "FROM users " +
              "WHERE id_group = ? AND id_role = 1 AND id_status <> 3 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_GROUP
            = "SELECT COUNT(id) " +
              "FROM users " +
              "WHERE id_group = ? AND id_role = 1 AND id_status <> 3;";

    private static final String INSERT
            = "INSERT users (login, email, password_hash, salt, id_role, id_status, id_group, first_name, patronymic, " +
              "last_name, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE users " +
              "SET email = ?, password_hash = ?, salt = ?, id_role = ?, id_status = ?, id_group = ?, first_name = ?, " +
              "patronymic = ?, last_name = ?, profile_image = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE users " +
              "SET id_status = 3 " +
              "WHERE id = ?;";

    private ResultSetMapper<User> userMapper;
    private ResultSetMapper<Long> longMapper;

    /**
     * Instantiate a new instance of {@code UserDaoImpl}.
     *
     * @param userMapper mapper for users.
     * @param longMapper mapper for {@code long} values.
     */
    @Inject
    public UserDaoImpl(ResultSetMapper<User> userMapper, ResultSetMapper<Long> longMapper) {
        this.userMapper = userMapper;
        this.longMapper = longMapper;
    }

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
    public void selectByRole(int offset, int limit, long roleId, List<User> result) throws DaoException {
        QueryExecutor.executeSelect(userMapper, SELECT_MULTIPLE_BY_ROLE, result, roleId, limit, offset);
    }

    @Override
    public long selectCountByRole(long roleId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_ROLE, roleId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_ROLE query"));
    }

    @Override
    public void selectByGroup(int offset, int limit, long groupId, List<User> result) throws DaoException {
        QueryExecutor.executeSelect(userMapper, SELECT_MULTIPLE_BY_GROUP, result, groupId, limit, offset);
    }

    @Override
    public long selectCountByGroup(long groupId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_GROUP, groupId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP query"));
    }

    @Override
    public long insert(User user) throws DaoException {
        long groupId = user.getGroupId();
        return QueryExecutor.executeInsert(
                INSERT,
                user.getLogin(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getSalt(),
                user.getRole().ordinal(),
                user.getStatus().getStatusId(),
                groupId != 0 ? groupId : null, // null for default long value
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getProfileImageName()
        );
    }

    @Override
    public void update(User user) throws DaoException {
        long groupId = user.getGroupId();
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                user.getEmail(),
                user.getPasswordHash(),
                user.getSalt(),
                user.getRole().ordinal(),
                user.getStatus().getStatusId(),
                groupId != 0 ? groupId : null, // null for default long value
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getProfileImageName(),
                user.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
