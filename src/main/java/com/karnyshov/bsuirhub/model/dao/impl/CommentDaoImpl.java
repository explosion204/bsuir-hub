package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.AssignmentDao;
import com.karnyshov.bsuirhub.model.dao.CommentDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Comment;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * {@code CommentDaoImpl} is an implementation of {@link CommentDao} interfaces.
 * @author Dmitry Karnyshov
 */
@Named
public class CommentDaoImpl implements CommentDao {
    private static final String SELECT_BY_ID
            = "SELECT id, id_grade, id_user, text, creation_time " +
              "FROM comments " +
              "WHERE id = ?;";

    private static final String SELECT_BY_GRADE
            = "SELECT id, id_grade, id_user, text, creation_time " +
              "FROM comments " +
              "WHERE id_grade = ? " +
              "ORDER BY creation_time DESC " +
              "LIMIT ? " +
              "OFFSET ?";

    private static final String SELECT_COUNT_BY_GRADE
            = "SELECT COUNT(id) " +
              "FROM comments " +
              "WHERE id_grade = ?;";

    private static final String INSERT
            = "INSERT comments (id_grade, id_user, text, creation_time) VALUES (?, ?, ?, ?);";

    private static final String DELETE
            = "DELETE FROM comments " +
              "WHERE id = ?;";

    private static final String DELETE_BY_GRADE
            = "DELETE FROM comments " +
              "WHERE id_grade = ?";

    private static final String DELETE_BY_STUDENT
            = "DELETE FROM comments " +
              "WHERE id_grade IN (SELECT id FROM grades WHERE id_student = ?);";


    private ResultSetMapper<Comment> commentMapper;
    private ResultSetMapper<Long> longMapper;

    /**
     * Instantiate a new instance of {@code CommentDaoImpl}.
     *
     * @param commentMapper mapper for comments.
     * @param longMapper mapper for {@code long} values.
     */
    @Inject
    public CommentDaoImpl(ResultSetMapper<Comment> commentMapper, ResultSetMapper<Long> longMapper) {
        this.commentMapper = commentMapper;
        this.longMapper = longMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Comment> result) throws DaoException {
        throw new UnsupportedOperationException("Implementation of CommentDao does not support selectAll operation");
    }

    @Override
    public long selectTotalCount() throws DaoException {
        throw new UnsupportedOperationException("Implementation of CommentDao does not support selectTotalCount operation");
    }

    @Override
    public Optional<Comment> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(commentMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByGrade(int offset, int limit, long gradeId, List<Comment> result) throws DaoException {
        QueryExecutor.executeSelect(commentMapper, SELECT_BY_GRADE, result, gradeId, limit, offset);
    }

    @Override
    public long selectCountByGrade(long gradeId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_GRADE, gradeId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GRADE query"));
    }

    @Override
    public long insert(Comment comment) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                comment.getGradeId(),
                comment.getUserId(),
                comment.getText(),
                comment.getCreationTime()
        );
    }

    @Override
    public void update(Comment comment) throws DaoException {
        throw new UnsupportedOperationException("Implementation of CommentDao does not support update operation");
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }

    @Override
    public void deleteByGrade(long gradeId) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE_BY_GRADE, gradeId);
    }

    @Override
    public void deleteByStudent(long studentId) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE_BY_STUDENT, studentId);
    }
}
