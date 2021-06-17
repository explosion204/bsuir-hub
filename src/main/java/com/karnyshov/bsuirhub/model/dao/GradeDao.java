package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeDao {
    List<Grade> selectAll() throws DaoException;
    Optional<Grade> selectById(long id) throws DaoException;
    void insert(Grade grade) throws DaoException;
    void update(Grade grade) throws DaoException;
    void delete(long id) throws DaoException;
}
