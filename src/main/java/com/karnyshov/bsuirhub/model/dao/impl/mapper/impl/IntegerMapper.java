package com.karnyshov.bsuirhub.model.dao.impl.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@code IntegerMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Integer} object.
 * @author Dmitry Karnyshov
 */
public class IntegerMapper implements ResultSetMapper<Integer> {
    @Override
    public Integer map(ResultSet resultSet) throws DaoException {
        try {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
