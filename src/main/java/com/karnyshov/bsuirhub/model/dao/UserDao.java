package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void selectAll(int offset, int limit, List<User> result) throws DaoException; // TODO: 7/8/2021
    long selectTotalCount() throws DaoException;

    Optional<User> selectById(long id) throws DaoException;

    Optional<User> selectByLogin(String login) throws DaoException;
    void selectByLogin(int offset, int limit, String keyword, List<User> result) throws DaoException;
    long selectCountByLogin(String keyword) throws DaoException;

    Optional<User> selectByEmail(String email) throws DaoException;
    void selectByEmail(int offset, int limit, String keyword, List<User> result) throws DaoException;
    long selectCountByEmail(String keyword) throws DaoException;

    void selectByLastName(int offset, int limit, String keyword, List<User> result) throws DaoException;
    long selectCountByLastName(String keyword) throws DaoException;

    long insert(User user) throws DaoException; // TODO: 6/17/2021 return value
    void update(User user) throws DaoException;
    void delete(long id) throws DaoException;
}
