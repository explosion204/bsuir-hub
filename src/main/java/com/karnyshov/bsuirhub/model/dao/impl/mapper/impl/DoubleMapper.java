package com.karnyshov.bsuirhub.model.dao.impl.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@code DoubleMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Double} object.
 * @author Dmitry Karnyshov
 */
public class DoubleMapper implements ResultSetMapper<Double> {
    @Override
    public Double map(ResultSet resultSet) throws DaoException {
        try {
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
