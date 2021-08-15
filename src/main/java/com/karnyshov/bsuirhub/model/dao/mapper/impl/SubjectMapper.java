package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.Subject;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

/**
 * {@code SubjectMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Subject} object.
 * @author Dmitry Karnyshov
 */
@Named
public class SubjectMapper implements ResultSetMapper<Subject> {
    @Override
    public Subject map(ResultSet resultSet) throws DaoException {
        try {
            return (Subject) Subject.builder()
                    .setName(resultSet.getString(SUBJECT_NAME))
                    .setShortName(resultSet.getString(SUBJECT_SHORT_NAME))
                    .setArchived(resultSet.getBoolean(SUBJECT_IS_ARCHIVED))
                    .setEntityId(resultSet.getLong(SUBJECT_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
