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

/**
 * {@code GroupDaoImpl} is an implementation of {@link GroupDao} interfaces.
 * @author Dmitry Karnyshov
 */
@Named
public class GroupDaoImpl implements GroupDao {
    private static final String SELECT_ALL
            = "SELECT id, name, id_department, id_headman, id_curator, is_archived " +
              "FROM `groups` " +
              "WHERE is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM `groups` " +
              "WHERE is_archived = 0;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, id_department, id_headman, id_curator, is_archived " +
              "FROM `groups` " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, id_department, id_headman, id_curator, is_archived " +
              "FROM `groups` " +
              "WHERE name = ? AND is_archived = 0;";

    private static final String SELECT_MULTIPLE_BY_NAME
            = "SELECT id, name, id_department, id_headman, id_curator, is_archived " +
              "FROM `groups` " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM `groups` " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String SELECT_BY_DEPARTMENT
            = "SELECT groups.id, groups.name, id_department, id_headman, id_curator, is_archived " +
              "FROM `groups` " +
              "WHERE id_department = ? AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_DEPARTMENT
            = "SELECT COUNT(groups.id) " +
              "FROM `groups` " +
              "WHERE id_department = ? AND is_archived = 0;";
    
    private static final String INSERT
            = "INSERT `groups` (name, id_department, id_headman, id_curator, is_archived) VALUES (?, ?, ?, ?, 0);";

    private static final String UPDATE
            = "UPDATE `groups` " +
              "SET name = ?, id_department = ?, id_headman = ?, id_curator = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE `groups` " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    private ResultSetMapper<Group> groupMapper;
    private ResultSetMapper<Integer> integerMapper;

    /**
     * Instantiate a new instance of {@code GroupDaoImpl}.
     *
     * @param groupMapper mapper for groups.
     * @param integerMapper mapper for {@code int} values.
     */
    @Inject
    public GroupDaoImpl(ResultSetMapper<Group> groupMapper, ResultSetMapper<Integer> integerMapper) {
        this.groupMapper = groupMapper;
        this.integerMapper = integerMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Group> result) throws DaoException {
        QueryExecutor.executeSelect(groupMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public int selectTotalCount() throws DaoException {
        Optional<Integer> result = QueryExecutor.executeSelectForSingleResult(integerMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<Group> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(groupMapper, SELECT_BY_ID, id);
    }

    @Override
    public Optional<Group> selectByName(String name) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(groupMapper, SELECT_BY_NAME, name);
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Group> result) throws DaoException {
        QueryExecutor.executeSelect(groupMapper, SELECT_MULTIPLE_BY_NAME, result, keyword, limit, offset);
    }

    @Override
    public int selectCountByName(String keyword) throws DaoException {
        Optional<Integer> result = QueryExecutor.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_NAME query"));
    }

    @Override
    public void selectByDepartment(int offset, int limit, long departmentId, List<Group> result)
                throws DaoException {
        QueryExecutor.executeSelect(groupMapper, SELECT_BY_DEPARTMENT, result, departmentId,
                limit, offset);
    }

    @Override
    public int selectCountByDepartment(long departmentId) throws DaoException {
        Optional<Integer> result = QueryExecutor.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_DEPARTMENT,
                departmentId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_FACULTY_AND_DEPARTMENT query"));
    }

    @Override
    public long insert(Group group) throws DaoException {
        long headmanId = group.getHeadmanId();
        return QueryExecutor.executeInsert(
                INSERT,
                group.getName(),
                group.getDepartmentId(),
                headmanId != 0 ? headmanId : null, // null for default long value
                group.getCuratorId()
        );
    }

    @Override
    public int update(Group group) throws DaoException {
        long headmanId = group.getHeadmanId();
        return QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                group.getName(),
                group.getDepartmentId(),
                headmanId != 0 ? headmanId : null, // null for default long value
                group.getCuratorId(),
                group.getEntityId()
        );
    }

    @Override
    public int delete(long id) throws DaoException {
        return QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
