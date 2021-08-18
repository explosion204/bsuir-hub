package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.*;

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
     * @return {@code int} value that represents count of stored comments with specified grade id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByGrade(long gradeId) throws DaoException;

    /**
     * Delete existing (strictly not necessarily) comments by specified {@link Grade} id.
     *
     * @param gradeId unique id of the grade.
     * @return number of affected records.
     * @throws DaoException if an error occurred while processing the query.
     */
    int deleteByGrade(long gradeId) throws DaoException;

    /**
     * Delete existing (strictly not necessarily) comments by specified {@link User Student} id.
     *
     * @param studentId unique id of the student.
     * @return number of affected records.
     * @throws DaoException if an error occurred while processing the query.
     */
    int deleteByStudent(long studentId) throws DaoException;
}
