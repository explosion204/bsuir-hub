package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.CommentDao;
import com.karnyshov.bsuirhub.model.entity.Comment;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class CommentDaoImpl implements CommentDao {
    private static final String SELECT_ALL
            = "SELECT id, id_grade, id_user, text " +
              "FROM comments " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM comments;";

    private static final String SELECT_BY_ID
            = "SELECT id, id_grade, id_user, text " +
              "FROM comments " +
              "WHERE id = ?;";

    private static final String SELECT_BY_GRADE
            = "SELECT id, id_grade, id_user, text " +
              "FROM comments " +
              "WHERE id_grade = ?;";

    private static final String SELECT_COUNT_BY_GRADE
            = "SELECT COUNT(id) " +
              "FROM comments " +
              "WHERE id_grade = ?;";

    private static final String INSERT
            = "INSERT comments (id_grade, id_user, text) VALUES (?, ?, ?);";

    private static final String UPDATE
            = "UPDATE comments " +
              "SET id_grade = ?, id_user = ?, text = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "DELETE FROM comments " +
              "WHERE id = ?;";


    @Override
    public void selectAll(int offset, int limit, List<Comment> result) throws DaoException {
        // TODO: 7/30/2021
    }

    @Override
    public long selectTotalCount() throws DaoException {
        return 0; // TODO: 7/30/2021
    }

    @Override
    public Optional<Comment> selectById(long id) throws DaoException {
        return Optional.empty(); // TODO: 7/30/2021
    }

    @Override
    public void selectByGrade(int offset, int limit, long gradeId, List<Comment> result) throws DaoException {
        // TODO: 7/30/2021
    }

    @Override
    public void selectCountByGrade(long gradeId) throws DaoException {
        // TODO: 7/30/2021
    }

    @Override
    public long insert(Comment entity) throws DaoException {
        return 0; // TODO: 7/30/2021
    }

    @Override
    public void update(Comment entity) throws DaoException {
        // TODO: 7/30/2021
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/30/2021
    }
}
