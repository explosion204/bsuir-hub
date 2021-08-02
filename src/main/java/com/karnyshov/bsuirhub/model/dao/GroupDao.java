package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends BaseDao<Group> {
    Optional<Group> selectByName(String name) throws DaoException;
    void selectByName(int offset, int limit, String keyword, List<Group> result) throws DaoException;
    long selectCountByName(String keyword) throws DaoException;

    void selectByDepartment(int offset, int limit, long departmentId, List<Group> result)
            throws DaoException;
    long selectCountByDepartment(long departmentId) throws DaoException;
}
