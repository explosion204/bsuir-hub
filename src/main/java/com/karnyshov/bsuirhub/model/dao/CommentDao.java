package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.List;


/**
 * {@code CommentDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Comment} entities.
 * @author Dmitry Karnyshov
 */
public interface CommentDao extends BaseDao<Comment> {
    /**
     * Select {@code Comment} entities by {@link Grade} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param gradeId unique id of the grade.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByGrade(int offset, int limit, long gradeId, List<Comment> result) throws DaoException;

    /**
     * Select count of {@code Comment} entities with specified {@link Grade} id.
     *
     * @param gradeId unique id of the grade.
     * @return {@code long} value that represents count of stored comments with specified grade id.
     * @throws DaoException if an error occurred while processing the query.
     */
    long selectCountByGrade(long gradeId) throws DaoException;
}
