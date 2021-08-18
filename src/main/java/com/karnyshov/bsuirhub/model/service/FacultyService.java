package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.criteria.FacultyFilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code FacultyService} is an interface with a bunch of methods that allow to manipulate {@link Faculty}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface FacultyService {
    /**
     * Find {@code Faculty} entity by its unique id.
     *
     * @param id unique id of the faculty.
     * @return {@code Faculty} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Faculty> findById(long id) throws ServiceException;

    /**
     * Find {@code Faculty} entities that satisfy certain criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param criteria instance of {@link FacultyFilterCriteria} that specifies a criteria for filtering.
     * @param keyword stored entities will be filtered by this value.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds) satisfied
     * filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, FacultyFilterCriteria criteria, String keyword, List<Faculty> result)
            throws ServiceException;

    /**
     * Find all {@code Faculty} entities omitting filter criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds).
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, List<Faculty> result) throws ServiceException;

    /**
     * Create a new {@code Faculty} entity in the data store.
     *
     * @param faculty the faculty to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Faculty faculty) throws ServiceException;

    /**
     * Update a {@code Faculty} entity in the data store.
     *
     * @param faculty the faculty to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Faculty faculty) throws ServiceException;

    /**
     * Delete a {@code Faculty} entity with specified id from the data store.
     *
     * @param id unique id of the faculty to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;
}
