package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

/**
 * {@code AssignmentMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Assignment} object.
 * @author Dmitry Karnyshov
 */
@Named
public class AssignmentMapper implements ResultSetMapper<Assignment> {
    @Override
    public Assignment map(ResultSet resultSet) throws DaoException {
        try {
            return (Assignment) Assignment.builder()
                    .setGroupId(resultSet.getLong(ASSIGNMENT_GROUP_ID))
                    .setTeacherId(resultSet.getLong(ASSIGNMENT_TEACHER_ID))
                    .setSubjectId(resultSet.getLong(ASSIGNMENT_SUBJECT_ID))
                    .setEntityId(resultSet.getLong(ASSIGNMENT_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
