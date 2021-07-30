package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class FacultyDaoImpl implements FacultyDao {
    private static final String SELECT_ALL
            = "SELECT id, name, short_name " +
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
            = "SELECT id, name, short_name " +
              "FROM faculties " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, short_name " +
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
            = "SELECT id, name, short_name " +
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
            = "INSERT faculties (name, short_name) VALUES (?, ?);";

    private static final String UPDATE
            = "UPDATE faculties " +
              "SET name = ?, short_name = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "UPDATE faculties " +
              "SET is_archived = 1 " +
              "WHERE id = ?;";

    @Override
    public void selectAll(int offset, int limit, List<Faculty> result) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public long selectTotalCount() throws DaoException {
        return 0; // TODO: 7/29/2021
    }

    @Override
    public Optional<Faculty> selectById(long id) throws DaoException {
        return Optional.empty(); // TODO: 7/29/2021
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public long selectCountByName(String keyword) {
        return 0; // TODO: 7/29/2021
    }

    @Override
    public void selectByShortName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public long selectCountByShortName(String keyword) {
        return 0; // TODO: 7/29/2021
    }

    @Override
    public long insert(Faculty entity) throws DaoException {
        return 0; // TODO: 7/29/2021
    }

    @Override
    public void update(Faculty entity) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/29/2021
    }
}

