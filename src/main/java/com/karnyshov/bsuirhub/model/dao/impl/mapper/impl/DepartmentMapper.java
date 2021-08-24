package com.karnyshov.bsuirhub.model.dao.impl.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Department;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

/**
 * {@code DepartmentMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link Department} object.
 * @author Dmitry Karnyshov
 */
public class DepartmentMapper implements ResultSetMapper<Department> {
    @Override
    public Department map(ResultSet resultSet) throws DaoException {
        try {
            return (Department) Department.builder()
                    .setName(resultSet.getString(DEPARTMENT_NAME))
                    .setShortName(resultSet.getString(DEPARTMENT_SHORT_NAME))
                    .setFacultyId(resultSet.getLong(DEPARTMENT_FACULTY_ID))
                    .setSpecialtyAlias(resultSet.getString(DEPARTMENT_SPECIALTY_ALIAS))
                    .setArchived(resultSet.getBoolean(DEPARTMENT_IS_ARCHIVED))
                    .setEntityId(resultSet.getLong(DEPARTMENT_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
