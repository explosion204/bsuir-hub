package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Grade;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;
import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class GradeMapper implements ResultSetMapper<Grade> {
    @Override
    public Grade map(ResultSet resultSet) throws DaoException {
        try {
            return (Grade) Grade.builder()
                    .setValue(Grade.Value.values()[resultSet.getInt(GRADE_VALUE)])
                    .setIsExam(resultSet.getBoolean(GRADE_IS_EXAM))
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
