package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * {@code GradeService} is an interface with a bunch of methods that allow to manipulate {@link Grade}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface GradeService {
    /**
     * Find {@code Grade} entity by its unique id.
     *
     * @param id unique id of the grade.
     * @return {@code Grade} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Grade> findById(long id) throws ServiceException;

    /**
     * Find {@code Grade} entities by specified {@link User Student} id and {@link Subject} id.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param studentId unique id of the student.
     * @param subjectId unique id of the subject.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code long} value that represents count of ALL entities (even beyond specified bounds) with specified
     * student id and subject id.
     * @throws ServiceException if an error occurred executing the method.
     */
    long findByStudentAndSubject(int start, int size, long studentId, long subjectId, List<Grade> result)
            throws ServiceException;

    /**
     * Calculate average grade value for all {@link Subject Subjects} by {@link User Student} id.
     *
     * @param studentId unique id of the student.
     * @return {@code double} value that represents calculated average grade value.
     * @throws ServiceException if an error occurred executing the method.
     */
    double calculateAverage(long studentId) throws ServiceException;

    /**
     * Calculate average grade value for specified {@link Subject} by {@link User Student} id.
     *
     * @param studentId unique id of the student.
     * @param subjectId unique id of the subject.
     * @return {@code double} value that represents calculated average grade value.
     * @throws ServiceException if an error occurred executing the method.
     */
    double calculateAverageBySubject(long studentId, long subjectId) throws ServiceException;

    /**
     * Create a new {@code Grade} entity in the data store.
     *
     * @param grade the faculty to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Grade grade) throws ServiceException;

    /**
     * Update a {@code Grade} entity in the data store.
     *
     * @param grade the grade to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Grade grade) throws ServiceException;

    /**
     * Delete a {@code Grade} entity with specified id from the data store.
     *
     * @param id unique id of the grade to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;

    /**
     * Delete a {@code Grade} entities with specified {@link User Student} id from the data store.
     *
     * @param studentId unique id of the student.
     * @throws ServiceException if an error occurred executing the method.
     */
    void deleteByStudent(long studentId) throws ServiceException;
}