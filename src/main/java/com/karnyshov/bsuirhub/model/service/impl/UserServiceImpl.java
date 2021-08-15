package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.service.criteria.UserFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * {@code UserServiceImpl} class is an implementation of {@link UserService} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class UserServiceImpl implements UserService {
    private static final int SALT_LENGTH = 16;
    private static final String EMPTY_PASSWORD = "";

    @Inject
    private UserDao userDao;

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        List<User> result = new LinkedList<>();

        try {
            int offset = 0;
            int limit = 1;
            userDao.selectByLogin(offset, limit, login, result);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        if (!result.isEmpty()) {
            User user = result.get(0);
            String dbPasswordHash = user.getPasswordHash();
            String salt = user.getSalt();
            String passwordHash = hashPassword(password + salt);

            if (Objects.equals(passwordHash, dbPasswordHash)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(long id) throws ServiceException {
        try {
            return userDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws ServiceException {
        try {
            return userDao.selectByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int start, int size, UserFilterCriteria criteria, String keyword, List<User> result)
                throws ServiceException {
        long totalUsers;
        
        try {
            switch (criteria) {
                case NONE:
                    userDao.selectAll(start, size, result);
                    totalUsers = userDao.selectTotalCount();
                    break;
                case LOGIN:
                    userDao.selectByLogin(start, size, keyword, result);
                    totalUsers = userDao.selectCountByLogin(keyword);
                    break;
                case EMAIL:
                    userDao.selectByEmail(start, size, keyword, result);
                    totalUsers = userDao.selectCountByEmail(keyword);
                    break;
                case LAST_NAME:
                    userDao.selectByLastName(start, size, keyword, result);
                    totalUsers = userDao.selectCountByLastName(keyword);
                    break;
                case ROLE:
                    long roleId = NumberUtils.isParsable(keyword) ? Long.parseLong(keyword) : 0;
                    userDao.selectByRole(start, size, roleId, result);
                    totalUsers = userDao.selectCountByRole(roleId);
                    break;
                case GROUP:
                    long groupId = NumberUtils.isParsable(keyword) ? Long.parseLong(keyword) : 0;
                    userDao.selectByGroup(start, size, groupId, result);
                    totalUsers = userDao.selectCountByGroup(groupId);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalUsers;
        } catch (DaoException | NumberFormatException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, List<User> result) throws ServiceException {
        return filter(page, pageSize, UserFilterCriteria.NONE, null, result);
    }

    @Override
    public String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    @Override
    public User changePassword(long id, String newPassword) throws ServiceException {
        try {
            User user = userDao.selectById(id)
                    .orElseThrow(() -> new ServiceException("Unable to find user with id " + id));

            String salt = RandomStringUtils.random(SALT_LENGTH, true, true);
            String passwordHash = hashPassword(newPassword + salt);

            User updatedUser = User.builder().of(user)
                    .setPasswordHash(passwordHash)
                    .setSalt(salt)
                    .build();

            userDao.update(updatedUser);
            return updatedUser;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User changeEmail(long id, String newEmail) throws ServiceException {
        try {
            User user = userDao.selectById(id)
                    .orElseThrow(() -> new ServiceException("Unable to find user with id " + id));

            User updatedUser = User.builder().of(user)
                    .setEmail(newEmail)
                    .setStatus(UserStatus.NOT_CONFIRMED)
                    .build();

            userDao.update(updatedUser);
            return updatedUser;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isLoginUnique(String login) throws ServiceException {
        try {
            Optional<User> user = userDao.selectByLogin(login);
            return user.isEmpty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isEmailUnique(String email) throws ServiceException {
        try {
            Optional<User> user = userDao.selectByEmail(email);
            return user.isEmpty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long create(User user, String password) throws ServiceException {
        String salt = RandomStringUtils.random(SALT_LENGTH, true, true);
        String passwordHash = DigestUtils.sha256Hex(password + salt);
        user = User.builder()
                .of(user)
                .setPasswordHash(passwordHash)
                .setSalt(salt)
                .build();

        try {
            return userDao.insert(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(User user, String password) throws ServiceException {
        String salt;
        String passwordHash;

        try {
            if (!StringUtils.isBlank(password)) {
                salt = RandomStringUtils.random(SALT_LENGTH, true, true);
                passwordHash = hashPassword(password + salt);
            } else {
                Optional<User> oldUser = userDao.selectById(user.getEntityId());

                if (oldUser.isEmpty()) {
                    throw new ServiceException("Unable to find user with id " + user.getEntityId());
                }

                salt = oldUser.get().getSalt();
                passwordHash = oldUser.get().getPasswordHash();
            }

            user = User.builder()
                    .of(user)
                    .setPasswordHash(passwordHash)
                    .setSalt(salt)
                    .build();

            userDao.update(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(User user) throws ServiceException {
        update(user, EMPTY_PASSWORD);
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            userDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
