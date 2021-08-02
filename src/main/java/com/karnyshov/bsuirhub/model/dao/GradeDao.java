package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Grade;

import java.util.List;

public interface GradeDao extends BaseDao<Grade> {
    void selectByStudentAndSubject(int offset, int limit, long studentId, long subjectId, List<Grade> result)
            throws DaoException;
    long selectCountByStudentAndSubject(long studentId, long subjectId) throws DaoException;
}
