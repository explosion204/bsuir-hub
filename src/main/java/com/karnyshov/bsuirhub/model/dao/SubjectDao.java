package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao {
     // List<Subject> selectAll() throws DaoException;
     Optional<Subject> selectById(long id) throws DaoException;
     List<Subject> selectByDepartment(long departmentId) throws DaoException;
     List<Subject> selectByName(String keyword) throws DaoException;
     void insert(Subject subject) throws DaoException;
     void update(Subject subject) throws DaoException;
}
