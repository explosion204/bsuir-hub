package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.DepartmentDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Department;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class DepartmentDaoImpl implements DepartmentDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name, id_faculty, specialty_alias, is_archived " +
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
            = "SELECT id, name, short_name, id_faculty, specialty_alias, is_archived " +
              "FROM departments " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, short_name, id_faculty, specialty_alias, is_archived " +
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
            = "SELECT id, name, short_name, id_faculty, specialty_alias, is_archived " +
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
            = "SELECT id, name, short_name, id_faculty, specialty_alias, is_archived " +
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
            = "INSERT departments (name, short_name, id_faculty, specialty_alias, is_archived) VALUES (?, ?, ?, ?, 0);";

    private static final String UPDATE
            = "UPDATE departments " +
              "SET name = ?, short_name = ?, id_faculty = ?, specialty_alias = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE departments " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    private ResultSetMapper<Department> departmentMapper;
    private ResultSetMapper<Long> longMapper;

    @Inject
    public DepartmentDaoImpl(ResultSetMapper<Department> departmentMapper, ResultSetMapper<Long> longMapper) {
        this.departmentMapper = departmentMapper;
        this.longMapper = longMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Department> result) throws DaoException {
        QueryExecutor.executeSelect(departmentMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public long selectTotalCount() throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<Department> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(departmentMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Department> result) throws DaoException {
        QueryExecutor.executeSelect(departmentMapper, SELECT_BY_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_NAME query"));
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Department> result) throws DaoException {
        QueryExecutor.executeSelect(departmentMapper, SELECT_BY_SHORT_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByShortName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_SHORT_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_SHORT_NAME query"));
    }

    @Override
    public void selectByFaculty(int offset, int limit, long facultyId, List<Department> result) throws DaoException {
        QueryExecutor.executeSelect(departmentMapper, SELECT_BY_FACULTY, result, facultyId, limit, offset);
    }

    @Override
    public long selectCountByFaculty(long facultyId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_FACULTY, facultyId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_FACULTY query"));
    }

    @Override
    public long insert(Department department) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                department.getName(),
                department.getShortName(),
                department.getFacultyId(),
                department.getSpecialtyAlias()
        );
    }

    @Override
    public void update(Department department) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                department.getName(),
                department.getShortName(),
                department.getFacultyId(),
                department.getSpecialtyAlias(),
                department.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
