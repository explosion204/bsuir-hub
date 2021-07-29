package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao extends BaseDao<Subject> {
     void selectByName(int offset, int limit, String keyword, List<Subject> result) throws DaoException;
     long selectCountByName(String keyword) throws DaoException;
}
