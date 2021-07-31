package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Department;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class DepartmentMapper implements ResultSetMapper<Department> {
    @Override
    public Department map(ResultSet resultSet) throws DaoException {
        try {
            return (Department) Department.builder()
                    .setName(resultSet.getString(DEPARTMENT_NAME))
                    .setShortName(resultSet.getString(DEPARTMENT_SHORT_NAME))
                    .setFacultyId(resultSet.getLong(DEPARTMENT_FACULTY_ID))
                    .setSpecialtyAlias(resultSet.getString(DEPARTMENT_SPECIALTY_ALIAS))
                    .setEntityId(resultSet.getLong(DEPARTMENT_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
