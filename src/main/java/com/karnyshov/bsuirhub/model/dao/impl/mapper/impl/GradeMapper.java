package com.karnyshov.bsuirhub.model.dao.impl.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Grade;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;
import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

/**
 * {@code GradeMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Grade} object.
 * @author Dmitry Karnyshov
 */
@Named
public class GradeMapper implements ResultSetMapper<Grade> {
    @Override
    public Grade map(ResultSet resultSet) throws DaoException {
        try {
            return (Grade) Grade.builder()
                    .setValue(resultSet.getByte(GRADE_VALUE))
                    .setTeacherId(resultSet.getLong(GRADE_TEACHER_ID))
                    .setStudentId(resultSet.getLong(GRADE_STUDENT_ID))
                    .setSubjectId(resultSet.getLong(GRADE_SUBJECT_ID))
                    .setDate(resultSet.getDate(GRADE_DATE).toLocalDate())
                    .setEntityId(resultSet.getLong(GRADE_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
