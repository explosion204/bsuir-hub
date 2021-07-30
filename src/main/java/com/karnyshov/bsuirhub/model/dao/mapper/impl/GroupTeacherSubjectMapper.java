package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.GroupTeacherSubject;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class GroupTeacherSubjectMapper implements ResultSetMapper<GroupTeacherSubject> {
    @Override
    public GroupTeacherSubject map(ResultSet resultSet) throws DaoException {
        try {
            return (GroupTeacherSubject) GroupTeacherSubject.builder()
                    .setGroupId(resultSet.getLong(GTS_GROUP_ID))
                    .setTeacherId(resultSet.getLong(GTS_TEACHER_ID))
                    .setSubjectId(resultSet.getLong(GTS_SUBJECT_ID))
                    .setEntityId(resultSet.getLong(GTS_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
