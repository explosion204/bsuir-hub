package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.SubjectDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Subject;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class SubjectDaoImpl implements SubjectDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name, is_archived " +
              "FROM subjects " +
              "WHERE is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM subjects " +
              "WHERE is_archived = 0;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, short_name, is_archived " +
              "FROM subjects " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, short_name, is_archived " +
              "FROM subjects " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM subjects " +
              "WHERE name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String SELECT_BY_SHORT_NAME
            = "SELECT id, name, short_name, is_archived " +
              "FROM subjects " +
              "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0 " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_SHORT_NAME
            = "SELECT COUNT(id) " +
              "FROM subjects " +
              "WHERE short_name LIKE CONCAT('%', ?, '%') AND is_archived = 0;";

    private static final String INSERT
            = "INSERT subjects (name, short_name, is_archived) VALUES (?, ?, 0);";

    private static final String UPDATE
            = "UPDATE subjects " +
              "SET name = ?, short_name = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE subjects " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    @Inject
    private ResultSetMapper<Subject> subjectMapper;

    @Inject
    private ResultSetMapper<Long> longMapper;


    @Override
    public void selectAll(int offset, int limit, List<Subject> result) throws DaoException {
        QueryExecutor.executeSelect(subjectMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public long selectTotalCount() throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<Subject> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(subjectMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Subject> result) throws DaoException {
        QueryExecutor.executeSelect(subjectMapper, SELECT_BY_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_NAME query"));
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Subject> result) throws DaoException {
        QueryExecutor.executeSelect(subjectMapper, SELECT_BY_SHORT_NAME, result, keyword, limit, offset);
    }

    @Override
    public long selectCountByShortName(String keyword) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_SHORT_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_SHORT_NAME query"));

    }

    @Override
    public long insert(Subject subject) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                subject.getName(),
                subject.getShortName()
        );
    }

    @Override
    public void update(Subject subject) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                subject.getName(),
                subject.getShortName(),
                subject.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
