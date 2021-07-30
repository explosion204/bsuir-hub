package com.karnyshov.bsuirhub.model.dao.mapper;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.AbstractEntity;

import java.sql.ResultSet;

public interface ResultSetMapper<T> {
    T map(ResultSet resultSet) throws DaoException;
}
