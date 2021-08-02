package com.karnyshov.bsuirhub.model.dao.mapper;

import com.karnyshov.bsuirhub.exception.DaoException;

import java.sql.ResultSet;

public interface ResultSetMapper<T> {
    T map(ResultSet resultSet) throws DaoException;
}
