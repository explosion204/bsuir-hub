package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends BaseDao<Group> {
    void selectByName(int offset, int limit, String keyword, List<Group> result) throws DaoException;
    long selectCountByName(String keyword) throws DaoException;

    void selectByFaculty(int offset, int limit, long facultyId, List<Group> result) throws DaoException;
    long selectCountByFaculty(long facultyId) throws DaoException;

    void selectByFacultyAndDepartment(int offset, int limit, long facultyId, long departmentId, List<Group> result)
            throws DaoException;
    long selectCountByFacultyAndDepartment(long facultyId, long departmentId);
}
