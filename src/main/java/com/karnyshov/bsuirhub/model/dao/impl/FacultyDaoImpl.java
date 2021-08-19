package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

/**
 * {@code FacultyDaoImpl} is an implementation of {@link FacultyDao} interfaces.
 * @author Dmitry Karnyshov
 */
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

    private ResultSetMapper<Faculty> facultyMapper;
    private ResultSetMapper<Integer> integerMapper;

    /**
     * Instantiate a new instance of {@code FacultyDaoImpl}.
     *
     * @param facultyMapper mapper for faculties.
     * @param integerMapper mapper for {@code int} values.
     */
    @Inject
    public FacultyDaoImpl(ResultSetMapper<Faculty> facultyMapper, ResultSetMapper<Integer> integerMapper) {
        this.facultyMapper = facultyMapper;
        this.integerMapper = integerMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Faculty> result) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(facultyMapper, SELECT_ALL, result, limit, offset);
    }

    @Override
    public int selectTotalCount() throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_TOTAL_COUNT);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_TOTAL_COUNT query"));
    }

    @Override
    public Optional<Faculty> selectById(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeSelectForSingleResult(facultyMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(facultyMapper, SELECT_BY_NAME, result, keyword, limit, offset);
    }

    @Override
    public int selectCountByName(String keyword) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_NAME query"));
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(facultyMapper, SELECT_BY_SHORT_NAME, result, keyword, limit, offset);
    }

    @Override
    public int selectCountByShortName(String keyword) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_SHORT_NAME, keyword);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_SHORT_NAME query"));
    }

    @Override
    public long insert(Faculty faculty) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeInsert(
                INSERT,
                faculty.getName(),
                faculty.getShortName()
        );
    }

    @Override
    public int update(Faculty faculty) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeUpdateOrDelete(
                UPDATE,
                faculty.getName(),
                faculty.getShortName(),
                faculty.getEntityId()
        );
    }

    @Override
    public int delete(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeUpdateOrDelete(DELETE, id);
    }
}

