package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Assignment;

import java.util.List;

public interface AssignmentDao extends BaseDao<Assignment> {
    void selectByGroup(int offset, int limit, long groupId, List<Assignment> result) throws DaoException;
    long selectCountByGroup(long groupId) throws DaoException;

    void selectByTeacher(int offset, int limit, long teacherId, List<Assignment> result) throws DaoException;
    long selectCountByTeacher(long teacherId) throws DaoException;

    long selectCountByGroupSubject(long groupId, long subjectId) throws DaoException;
    long selectCountByGroupTeacherSubject(long groupId, long teacherId, long subjectId) throws DaoException;
}
