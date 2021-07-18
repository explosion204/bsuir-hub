package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Objects;
import java.util.Optional;

@Named
@Singleton
public class UserServiceImpl implements UserService {
    @Inject
    private UserDao userDao;

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        Optional<User> user;

        try {
             user = userDao.selectByLogin(login);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        if (user.isPresent()) {
            String dbPasswordHash = user.get().getPasswordHash();
            String salt = user.get().getSalt();
            String passwordHash = DigestUtils.sha256Hex(password + salt);

            if (Objects.equals(passwordHash, dbPasswordHash)) {
                return user;
            }
        }

        return Optional.empty();
    }

    @Override
    public void createUser(User user, String password, String confirmPassword) throws ServiceException {
        // TODO: 7/12/2021
    }

    @Override
    public void updateUser(User user, String password, String newPassword) throws ServiceException {
        // TODO: 7/12/2021
    }

    @Override
    public void deleteUser(String login) {
        // TODO: 7/12/2021
    }
}
