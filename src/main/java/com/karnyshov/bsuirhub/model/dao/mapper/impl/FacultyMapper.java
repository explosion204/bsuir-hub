package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;
import static com.karnyshov.bsuirhub.model.dao.TableColumn.FACULTY_ID;

@Named
public class FacultyMapper implements ResultSetMapper<Faculty> {
    @Override
    public Faculty map(ResultSet resultSet) throws DaoException {
        try {
            return (Faculty) Faculty.builder()
                    .setName(resultSet.getString(FACULTY_NAME))
                    .setShortName(resultSet.getString(FACULTY_SHORT_NAME))
                    .setEntityId(resultSet.getLong(FACULTY_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
