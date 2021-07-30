package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentDao extends BaseDao<Department> {
    Optional<Department> selectById(long id) throws DaoException;

    void selectByName(int offset, int limit, String keyword, List<Department> result) throws DaoException;
    long selectCountByName(String keyword) throws DaoException;

    void selectByShortName(int offset, int limit, String keyword, List<Department> result) throws DaoException;
    long selectCountByShortName(String keyword) throws DaoException;

    void selectByFaculty(int offset, int limit, long facultyId, List<Department> result) throws DaoException;
    void selectCountByFaculty(long facultyId) throws DaoException;
}
