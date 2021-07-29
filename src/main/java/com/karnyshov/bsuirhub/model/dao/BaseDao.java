package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    void selectAll(int offset, int limit, List<T> result) throws DaoException;
    long selectTotalCount() throws DaoException;
    Optional<T> selectById(long id) throws DaoException;
    long insert(T entity) throws DaoException;
    void update(T entity) throws DaoException;
    void delete(long id) throws DaoException;
}
