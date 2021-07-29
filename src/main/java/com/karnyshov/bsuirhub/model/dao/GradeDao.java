package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeDao extends BaseDao<Grade> {
    void selectByUserAndSubject(int offset, int limit, long userId, long subjectId, List<Grade> result) throws DaoException;
    long selectCountByUserAndSubject(long userId, long subjectId) throws DaoException;
}
