package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.SubjectDao;
import com.karnyshov.bsuirhub.model.entity.Subject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class SubjectDaoImpl implements SubjectDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name, is_archived " +
              "FROM subjects " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM subjects;";

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
            = "INSERT subjects (name, short_name, is_archived) VALUES (?, ?, ?);";

    private static final String UPDATE
            = "UPDATE subjects " +
              "SET name = ?, short_name = ?, is_archived = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE subjects " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";


    @Override
    public void selectAll(int offset, int limit, List<Subject> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectTotalCount() throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public Optional<Subject> selectById(long id) throws DaoException {
        return Optional.empty(); // TODO: 7/29/2021  
    }

    @Override
    public long insert(Subject entity) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void update(Subject entity) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Subject> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Subject> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByShortName(String keyword) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }
}
