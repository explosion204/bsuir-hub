package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.GroupDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Group;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class GroupDaoImpl implements GroupDao {
    private static final String SELECT_ALL
            = "SELECT id, name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM groups;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "WHERE name LIKE CONCAT('%', ?, '%') " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM groups " +
              "WHERE name LIKE CONCAT('%', ?, '%');";

    private static final String SELECT_BY_DEPARTMENT
            = "SELECT groups.id, groups.name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "WHERE id_department = ? " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_DEPARTMENT
            = "SELECT COUNT(groups.id) " +
              "FROM groups " +
              "INNER JOIN departments " +
              "ON departments.id = id_department AND departments.id_faculty = ? " +
              "WHERE id_department = ?;";
    
    private static final String INSERT
            = "INSERT groups (name, id_department, id_headman, id_curator) VALUES (?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE groups " +
              "SET name = ?, id_department = ?, id_headman = ?, id_curator = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "DELETE FROM groups " +
              "WHERE id = ?;";

    @Inject
    private ResultSetMapper<Group> groupMapper;

    @Inject
    private ResultSetMapper<Long> longMapper;

    @Override
    public void selectAll(int offset, int limit, List<Group> result) throws DaoException {
        QueryExecutor.executeSelect(groupMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public long selectTotalCount() throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<Group> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(groupMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Group> result) throws DaoException {
        QueryExecutor.executeSelect(groupMapper, SELECT_BY_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_NAME query"));
    }

    @Override
    public void selectByDepartment(int offset, int limit, long departmentId, List<Group> result)
                throws DaoException {
        QueryExecutor.executeSelect(groupMapper, SELECT_BY_DEPARTMENT, result, departmentId,
                limit, offset);
    }

    @Override
    public long selectCountByDepartment(long departmentId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_DEPARTMENT,
                departmentId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_FACULTY_AND_DEPARTMENT query"));
    }

    @Override
    public long insert(Group group) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                group.getName(),
                group.getDepartmentId(),
                group.getHeadmanId(),
                group.getCuratorId()
        );
    }

    @Override
    public void update(Group group) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                group.getName(),
                group.getDepartmentId(),
                group.getHeadmanId(),
                group.getCuratorId(),
                group.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
