package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> selectAll() throws DaoException;
    Optional<User> selectById(long id) throws DaoException;
    Optional<User> selectByLogin(String login) throws DaoException;
    void insert(User user) throws DaoException;
    void update(User user) throws DaoException;
}
