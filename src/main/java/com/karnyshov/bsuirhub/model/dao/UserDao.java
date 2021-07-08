package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> selectAll() throws DaoException; // TODO: 7/8/2021
    Optional<User> selectById(long id) throws DaoException;
    Optional<User> selectByLogin(String login) throws DaoException;
    Optional<User> selectByLastName(String keyword) throws DaoException;
    void insert(User user) throws DaoException; // TODO: 6/17/2021 return value
    void update(User user) throws DaoException;

    // TODO: 6/17/2021 finish dao
}
