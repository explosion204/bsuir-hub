package com.karnyshov.bsuirhub.model.dao.impl.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Faculty;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;
import static com.karnyshov.bsuirhub.model.dao.TableColumn.FACULTY_ID;

/**
 * {@code FacultyMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Faculty} object.
 * @author Dmitry Karnyshov
 */
public class FacultyMapper implements ResultSetMapper<Faculty> {
    @Override
    public Faculty map(ResultSet resultSet) throws DaoException {
        try {
            return (Faculty) Faculty.builder()
                    .setName(resultSet.getString(FACULTY_NAME))
                    .setShortName(resultSet.getString(FACULTY_SHORT_NAME))
                    .setArchived(resultSet.getBoolean(FACULTY_IS_ARCHIVED))
                    .setEntityId(resultSet.getLong(FACULTY_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
