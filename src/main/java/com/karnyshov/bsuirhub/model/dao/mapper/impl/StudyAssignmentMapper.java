package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class StudyAssignmentMapper implements ResultSetMapper<Assignment> {
    @Override
    public Assignment map(ResultSet resultSet) throws DaoException {
        try {
            return (Assignment) Assignment.builder()
                    .setGroupId(resultSet.getLong(STUDY_ASSIGNMENT_GROUP_ID))
                    .setTeacherId(resultSet.getLong(STUDY_ASSIGNMENT_TEACHER_ID))
                    .setSubjectId(resultSet.getLong(STUDY_ASSIGNMENT_SUBJECT_ID))
                    .setEntityId(resultSet.getLong(STUDY_ASSIGNMENT_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
