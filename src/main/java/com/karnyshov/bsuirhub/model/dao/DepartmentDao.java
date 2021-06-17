package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentDao {
    List<Department> selectAll() throws DaoException;
    Optional<Department> selectById(long id) throws DaoException;
    void insert(Department department) throws DaoException;
    void update(Department department) throws DaoException;
}
