package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.CommentDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
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
    private static final Logger logger = LogManager.getLogger();
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


    private ResultSetMapper<Comment> commentMapper;
    private ResultSetMapper<Integer> integerMapper;

    /**
     * Instantiate a new instance of {@code CommentDaoImpl}.
     *
     * @param commentMapper mapper for comments.
     * @param integerMapper mapper for {@code int} values.
     */
    @Inject
    public CommentDaoImpl(ResultSetMapper<Comment> commentMapper, ResultSetMapper<Integer> integerMapper) {
        this.commentMapper = commentMapper;
        this.integerMapper = integerMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Comment> result) throws DaoException {
        logger.error("Implementation of CommentDao does not support selectAll operation");
        throw new UnsupportedOperationException();
    }

    @Override
    public int selectTotalCount() throws DaoException {
        logger.error("Implementation of CommentDao does not support selectTotalCount operation");
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Comment> selectById(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeSelectForSingleResult(commentMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByGrade(int offset, int limit, long gradeId, List<Comment> result) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(commentMapper, SELECT_BY_GRADE, result, gradeId, limit, offset);
    }

    @Override
    public int selectCountByGrade(long gradeId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_GRADE, gradeId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GRADE query"));
    }

    @Override
    public long insert(Comment comment) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeInsert(
                INSERT,
                comment.getGradeId(),
                comment.getUserId(),
                comment.getText(),
                comment.getCreationTime()
        );
    }

    @Override
    public int update(Comment comment) throws DaoException {
        logger.error("Implementation of CommentDao does not support update operation");
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeUpdateOrDelete(DELETE, id);
    }
}
