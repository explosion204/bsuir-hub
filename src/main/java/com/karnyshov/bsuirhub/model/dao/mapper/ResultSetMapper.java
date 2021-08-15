package com.karnyshov.bsuirhub.model.dao.mapper;

import com.karnyshov.bsuirhub.exception.DaoException;

import java.sql.ResultSet;

/**
 * {@code ResultSetMapper} interface is responsible for mapping retrieved {@link ResultSet} object to specified object.
 *
 * @param <T> type of the object to map.
 * @author Dmitry Karnyshov
 */
@FunctionalInterface
public interface ResultSetMapper<T> {
    /**
     * Map {@link ResultSet} object to {@link T} object.
     *
     * @param resultSet {@code ResultSet} instance.
     * @return {@code T} instance.
     * @throws DaoException if an error occurred while retrieving data from {@code ResultSet}.
     */
    T map(ResultSet resultSet) throws DaoException;
}
