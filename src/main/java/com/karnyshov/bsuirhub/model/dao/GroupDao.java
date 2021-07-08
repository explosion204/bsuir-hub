package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    // ok
    Optional<Group> selectById(long id) throws DaoException;
    List<Group> selectByDepartment(long departmentId) throws DaoException;
    List<Group> selectByDepartmentAndNumber(long departmentId, String keyword) throws DaoException;
    void insert(Group group) throws DaoException;
    void update(Group group) throws DaoException;
}
