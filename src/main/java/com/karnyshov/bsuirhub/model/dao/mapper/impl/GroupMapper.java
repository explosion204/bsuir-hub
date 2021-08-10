package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Group;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class GroupMapper implements ResultSetMapper<Group> {
    @Override
    public Group map(ResultSet resultSet) throws DaoException {
        try {
            return (Group) Group.builder()
                    .setDepartmentId(resultSet.getLong(GROUP_DEPARTMENT_ID))
                    .setHeadmanId(resultSet.getLong(GROUP_HEADMAN_ID))
                    .setCuratorId(resultSet.getLong(GROUP_CURATOR_ID))
                    .setName(resultSet.getString(GROUP_NAME))
                    .setArchived(resultSet.getBoolean(GROUP_IS_ARCHIVED))
                    .setEntityId(resultSet.getLong(GROUP_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
