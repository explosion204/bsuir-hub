package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;

/**
 * {@code GradeDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Grade} entities.
 * @author Dmitry Karnyshov
 */
public interface GradeDao extends BaseDao<Grade> {
    /**
     * Select {@code Grade} entities by specified {@link User Student} id and {@link Subject} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param studentId unique id of the student.
     * @param subjectId unique id of the subject.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByStudentAndSubject(int offset, int limit, long studentId, long subjectId, List<Grade> result)
            throws DaoException;

    /**
     * Select count of {@code Grade} entities with specified {@link User Teacher} id and {@link Subject} id.
     *
     * @param studentId unique id of the student.
     * @param subjectId unique id of the subject.
     * @return {@code long} value that represents count of stored grades with teacher id and group id.
     * @throws DaoException if an error occurred while processing the query.
     */
    long selectCountByStudentAndSubject(long studentId, long subjectId) throws DaoException;

    /**
     * Select average grade value by specified {@link User Student} id.
     *
     * @param studentId unique id of the student.
     * @return {@code double} value that represents average grade value of the student.
     * @throws DaoException if an error occurred while processing the query.
     */
    double selectAverage(long studentId) throws DaoException;

    /**
     * Select average grade value by specified {@link User Student} id and {@link Subject} id.
     *
     * @param studentId unique id of the student.
     * @param subjectId unique id of the subject.
     * @return {@code double} value that represents average grade value of the student by specified subject.
     * @throws DaoException if an error occurred while processing the query.
     */
    double selectAverageBySubject(long studentId, long subjectId) throws DaoException;
}
