package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;

import java.util.List;

public interface StudyAssignmentDao extends BaseDao<StudyAssignment> {
    void selectByGroup(int offset, int limit, long groupId, List<StudyAssignment> result) throws DaoException;
    long selectCountByGroup(long groupId) throws DaoException;

    void selectByTeacher(int offset, int limit, long teacherId, List<StudyAssignment> result) throws DaoException;
    long selectCountByTeacher(long teacherId) throws DaoException;
}
