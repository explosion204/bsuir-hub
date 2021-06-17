package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.DepartmentDao;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {
    private static final String SELECT_ALL_DEPARTMENTS
            = "SELECT id, name, short_name, is_archived, id_faculty, specialty_alias " +
              "FROM departments";

    private static final String SELECT_DEPARTMENT_BY_ID
            = "SELECT id, name, short_name, is_archived, id_faculty, specialty_alias " +
              "FROM departments " +
              "WHERE id = ?";

    private static final String INSERT_DEPARTMENT
            = "INSERT departments (name, short_name, is_archived, id_faculty, specialty_alias) " +
              "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_DEPARTMENT
            = "UPDATE departments " +
              "SET name = ?, short_name = ?, is_archived = ?, id_faculty = ?, specialty_alias = ? " +
              "WHERE id = ?";

    @Override
    public List<Department> selectAll() throws DaoException {
        List<Department> departments = new LinkedList<>();
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_DEPARTMENTS);

            while (resultSet.next()) {
                Department department = extractDepartment(resultSet);
                departments.add(department);
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return departments;
    }

    @Override
    public Optional<Department> selectById(long id) throws DaoException {
        Department department;
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_DEPARTMENT_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            department = resultSet.next() ? extractDepartment(resultSet) : null;
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return department != null ? Optional.of(department) : Optional.empty();
    }

    @Override
    public void insert(Department department) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_DEPARTMENT);
            setupPreparedStatement(statement, department);
            statement.execute();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public void update(Department department) throws DaoException {
        Connection connection = null;
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

        try {
            connection = pool.acquireConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_DEPARTMENT);
            setupPreparedStatement(statement, department);
            statement.setLong(6, department.getEntityId());
            statement.execute();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private void setupPreparedStatement(PreparedStatement statement, Department department)
                throws SQLException {
        statement.setString(1, department.getName());
        statement.setString(2, department.getShortName());
        statement.setBoolean(3, department.isArchived());
        statement.setLong(4, department.getFacultyId());
        statement.setString(5, department.getSpecialtyAlias());
    }

    private Department extractDepartment(ResultSet resultSet) throws SQLException {
        long departmentId = resultSet.getLong(DEPARTMENT_ID);
        String name = resultSet.getString(DEPARTMENT_NAME);
        String shortName = resultSet.getString(DEPARTMENT_SHORT_NAME);
        boolean archived = resultSet.getBoolean(DEPARTMENT_IS_ARCHIVED);
        long facultyId = resultSet.getLong(DEPARTMENT_FACULTY_ID);
        String specialtyAlias = resultSet.getString(DEPARTMENT_SPECIALTY_ALIAS);

        return new Department(departmentId, name, shortName, archived, facultyId, specialtyAlias);
    }
}
