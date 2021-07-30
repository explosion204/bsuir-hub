package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

@Named
public class LongMapper implements ResultSetMapper<Long> {
    @Override
    public Long map(ResultSet resultSet) throws DaoException {
        try {
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
