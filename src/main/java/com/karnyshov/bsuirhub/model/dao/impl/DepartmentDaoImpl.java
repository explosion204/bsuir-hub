package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.DepartmentDao;
import com.karnyshov.bsuirhub.model.entity.Department;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class DepartmentDaoImpl implements DepartmentDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name, id_faculty, specialty_alias " +
              "FROM departments " +
              "WHERE is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM departments " +
              "WHERE is_archived = 0;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, short_name, id_faculty, specialty_alias " +
              "FROM departments " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, short_name, id_faculty, specialty_alias " +
              "FROM departments " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM departments " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String SELECT_BY_SHORT_NAME
            = "SELECT id, name, short_name, id_faculty, specialty_alias " +
              "FROM departments " +
              "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_SHORT_NAME
            = "SELECT COUNT(id) " +
              "FROM departments " +
              "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String SELECT_BY_FACULTY
            = "SELECT id, name, short_name, id_faculty, specialty_alias " +
              "FROM departments " +
              "WHERE id_faculty = ? AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_FACULTY
            = "SELECT COUNT(id) " +
              "FROM departments " +
              "WHERE id_faculty = ? AND is_archived = 0;";

    private static final String INSERT
            = "INSERT departments (name, short_name, id_faculty, specialty_alias) VALUES (?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE departments " +
              "SET name = ?, short_name = ?, id_faculty = ?, specialty_alias = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE departments " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    @Override
    public void selectAll(int offset, int limit, List<Department> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectTotalCount() throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public Optional<Department> selectById(long id) throws DaoException {
        return Optional.empty(); // TODO: 7/29/2021  
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Department> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByName(String keyword) {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Department> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByShortName(String keyword) {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void selectByFaculty(int offset, int limit, long facultyId, List<Department> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public void selectCountByFaculty(long facultyId) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long insert(Department entity) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void update(Department entity) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/29/2021
    }
}
