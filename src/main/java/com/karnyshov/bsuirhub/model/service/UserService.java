package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> authenticate(String login, String password) throws ServiceException;
    void createUser(User user, String password, String confirmPassword) throws ServiceException;
    void updateUser(User user, String password, String newPassword) throws ServiceException;
    void deleteUser(String login);
}
