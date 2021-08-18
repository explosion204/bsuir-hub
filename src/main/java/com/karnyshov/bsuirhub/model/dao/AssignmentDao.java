package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;

/**
 * {@code AssignmentDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Assignment} entities.
 * @author Dmitry Karnyshov
 */
public interface AssignmentDao extends BaseDao<Assignment> {
    /**
     * Select {@code Assignment} entities by {@link Group} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param groupId unique id of the group.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByGroup(int offset, int limit, long groupId, List<Assignment> result) throws DaoException;

    /**
     * Select count of {@code Assignment} entities with specified {@link Group} id.
     *
     * @param groupId unique id of the group.
     * @return {@code int} value that represents count of stored assignments with specified group id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByGroup(long groupId) throws DaoException;

    /**
     * Select {@code Assignment} entities by {@link User Teacher} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param teacherId unique id of the teacher.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByTeacher(int offset, int limit, long teacherId, List<Assignment> result) throws DaoException;

    /**
     * Select count of {@code Assignment} entities with specified {@link User Teacher} id.
     *
     * @param teacherId unique id of the teacher
     * @return {@code int} value that represents count of stored assignments with specified teacher id.
     * @throws DaoException if an error occurred while processing the query.n
     */
    int selectCountByTeacher(long teacherId) throws DaoException;

    /**
     * Select count of {@code Assignment} entities with {@link Group} id and {@link Subject} id.
     *
     * @param groupId unique id of the group.
     * @param subjectId unique id of the subjects/
     * @return {@code int} value that represents count of stored assignments with specified group id and subject id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByGroupSubject(long groupId, long subjectId) throws DaoException;

    /**
     * Select count of {@code Assignment} entities with {@link Group} id, {@link User Teacher} id
     * and {@link Subject} id.
     *
     * @param groupId unique id of the group.
     * @param teacherId unique id of the teacher.
     * @param subjectId unique id of the subject.
     * @return {@code int} value that represents count of stored assignments with specified group id, teacher id
     * and subject id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByGroupTeacherSubject(long groupId, long teacherId, long subjectId) throws DaoException;
}
