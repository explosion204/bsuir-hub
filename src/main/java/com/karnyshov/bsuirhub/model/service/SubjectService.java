package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.service.criteria.SubjectFilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code SubjectService} is an interface with a bunch of methods that allow to manipulate {@link Subject}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface SubjectService {
    /**
     * Find {@code Subject} entity by its unique id.
     *
     * @param id unique id of the subject.
     * @return {@code Subject} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Subject> findById(long id) throws ServiceException;

    /**
     * Find {@code Subject} entities that satisfy certain criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param criteria instance of {@link SubjectFilterCriteria} that specifies a criteria for filtering.
     * @param keyword stored entities will be filtered by this value.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds) satisfied
     * filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, SubjectFilterCriteria criteria, String keyword, List<Subject> result)
            throws ServiceException;

    /**
     * Find all {@code Subject} entities omitting filter criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds).
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, List<Subject> result) throws ServiceException;

    /**
     * Create a new {@code Subject} entity in the data store.
     *
     * @param subject the subject to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Subject subject) throws ServiceException;

    /**
     * Update a {@code Subject} entity in the data store.
     *
     * @param subject the subject to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Subject subject) throws ServiceException;

    /**
     * Delete a {@code Subject} entity with specified id from the data store.
     *
     * @param id unique id of the subject to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;
}
