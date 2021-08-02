package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;


@Named
public class FacultyDaoImpl implements FacultyDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name, is_archived " +
              "FROM faculties " +
              "WHERE is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM faculties " +
              "WHERE is_archived = 0;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, short_name, is_archived " +
              "FROM faculties " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, short_name, is_archived " +
              "FROM faculties " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM faculties " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String SELECT_BY_SHORT_NAME
            = "SELECT id, name, short_name, is_archived " +
            "FROM faculties " +
            "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
            "ORDER BY id " +
            "LIMIT ? " +
            "OFFSET ?;";

    private static final String SELECT_COUNT_BY_SHORT_NAME
            = "SELECT COUNT(id) " +
              "FROM faculties " +
              "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String INSERT
            = "INSERT faculties (name, short_name, is_archived) VALUES (?, ?, 0);";

    private static final String UPDATE
            = "UPDATE faculties " +
              "SET name = ?, short_name = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE faculties " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    @Inject
    private ResultSetMapper<Faculty> facultyMapper;

    @Inject
    private ResultSetMapper<Long> longMapper;

    @Override
    public void selectAll(int offset, int limit, List<Faculty> result) throws DaoException {
        QueryExecutor.executeSelect(facultyMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public long selectTotalCount() throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<Faculty> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(facultyMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        QueryExecutor.executeSelect(facultyMapper, SELECT_BY_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_NAME query"));
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        QueryExecutor.executeSelect(facultyMapper, SELECT_BY_SHORT_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByShortName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_SHORT_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_SHORT_NAME query"));
    }

    @Override
    public long insert(Faculty faculty) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                faculty.getName(),
                faculty.getShortName()
        );
    }

    @Override
    public void update(Faculty faculty) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                faculty.getName(),
                faculty.getShortName(),
                faculty.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}

