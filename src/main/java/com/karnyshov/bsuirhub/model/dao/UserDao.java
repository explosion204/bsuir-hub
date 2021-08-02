package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends BaseDao<User> {
    Optional<User> selectByLogin(String login) throws DaoException;
    void selectByLogin(int offset, int limit, String keyword, List<User> result) throws DaoException;
    long selectCountByLogin(String keyword) throws DaoException;

    Optional<User> selectByEmail(String email) throws DaoException;
    void selectByEmail(int offset, int limit, String keyword, List<User> result) throws DaoException;
    long selectCountByEmail(String keyword) throws DaoException;

    void selectByLastName(int offset, int limit, String keyword, List<User> result) throws DaoException;
    long selectCountByLastName(String keyword) throws DaoException;

    void selectByRole(int offset, int limit, long roleId, List<User> result) throws DaoException;
    long selectCountByRole(long roleId) throws DaoException;

    void selectByGroup(int offset, int limit, long groupId, List<User> result) throws DaoException;
    long selectCountByGroup(long groupId) throws DaoException;
}
