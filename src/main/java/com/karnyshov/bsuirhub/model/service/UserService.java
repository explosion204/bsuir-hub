package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.criteria.UserFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> authenticate(String login, String password) throws ServiceException;
    Optional<User> findById(long id) throws ServiceException;
    Optional<User> findByLogin(String login) throws ServiceException;
    Optional<User> findByEmail(String email) throws ServiceException;
    long filter(int page, int pageSize, UserFilterCriteria criteria, String keyword, List<User> result) throws ServiceException;
    long filter(int page, int pageSize, List<User> result) throws ServiceException;

    boolean isLoginUnique(String login);
    boolean isEmailUnique(String email);

    void create(User user, String password) throws ServiceException;
    void update(User user, String password) throws ServiceException;
    void update(User user) throws ServiceException;
    void delete(long id) throws ServiceException;
}
