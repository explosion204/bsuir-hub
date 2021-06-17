package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao {
     List<Subject> selectAll() throws DaoException;
     Optional<Subject> selectById(long id) throws DaoException;
     void insert(Subject subject) throws DaoException;
     void update(Subject subject) throws DaoException;
     boolean attachToTeacher(long subjectId, long teacherId);
     boolean detachFromTeacher(long subjectId, long teacherId);
     boolean attachToDepartment(long subjectId, long departmentId);
     boolean detachFromDepartment(long subjectId, long departmentId);
}
