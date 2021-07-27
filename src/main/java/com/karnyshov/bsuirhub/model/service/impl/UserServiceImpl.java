package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.service.criteria.UserFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Named
@Singleton // TODO: 7/23/2021 check singleton
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
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
            String passwordHash = DigestUtils.sha256Hex(password + salt);

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
    public Optional<User> findByLogin(String login) throws ServiceException {
        try {
            return userDao.selectByLogin(login);
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
    public long filter(int page, int pageSize, UserFilterCriteria criteria, String keyword, List<User> result)
                throws ServiceException {

        int offset = pageSize * (page - 1);
        long totalUsers;
        
        try {
            switch (criteria) {
                case NONE:
                    userDao.selectAll(offset, pageSize, result);
                    totalUsers = userDao.selectTotalCount();
                    break;
                case LOGIN:
                    userDao.selectByLogin(offset, pageSize, keyword, result);
                    totalUsers = userDao.selectCountByLogin(keyword);
                    break;
                case EMAIL:
                    userDao.selectByEmail(offset, pageSize, keyword, result);
                    totalUsers = userDao.selectCountByEmail(keyword);
                    break;
                case LAST_NAME:
                    userDao.selectByLastName(offset, pageSize, keyword, result);
                    totalUsers = userDao.selectCountByLastName(keyword);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalUsers;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, List<User> result) throws ServiceException {
        return filter(page, pageSize, UserFilterCriteria.NONE, null, result);
    }

    @Override
    public void changePassword(long id, String newPassword) throws ServiceException {
        try {
            User user = userDao.selectById(id)
                    .orElseThrow(() -> new ServiceException("Unable to find user with id " + id));

            String salt = RandomStringUtils.random(SALT_LENGTH, true, true);
            String passwordHash = DigestUtils.sha256Hex(newPassword + salt);

            User updatedUser = User.builder().of(user)
                    .setPasswordHash(passwordHash)
                    .setSalt(salt)
                    .build();

            userDao.update(updatedUser);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isLoginUnique(String login) {
        Optional<User> user; // TODO: 7/27/2021 caching

        try {
            user = userDao.selectByLogin(login);
        } catch (DaoException e) {
            logger.error("Something went wrong (UserService.isLoginUnique)", e);
            return false;
        }

        return user.isEmpty();
    }

    @Override
    public boolean isEmailUnique(String email) {
        Optional<User> user; // TODO: 7/27/2021 caching

        try {
            user = userDao.selectByEmail(email);
        } catch (DaoException e) {
            logger.error("Something went wrong (UserService.isEmailUnique)", e);
            return false;
        }

        return user.isEmpty();
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
                passwordHash = DigestUtils.sha256Hex(password + salt);
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
