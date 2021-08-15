package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.criteria.FacultyFilterCriteria;
import com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code GroupService} is an interface with a bunch of methods that allow to manipulate {@link Group}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface GroupService {
    /**
     * Find {@code Group} entity by its unique id.
     *
     * @param id unique id of the group.
     * @return {@code Group} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Group> findById(long id) throws ServiceException;

    /**
     * Find {@code Group} entities that satisfy certain criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param criteria instance of {@link GroupFilterCriteria} that specifies a criteria for filtering.
     * @param keyword stored entities will be filtered by this value.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code long} value that represents count of ALL entities (even beyond specified bounds) satisfied
     * filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    long filter(int start, int size, GroupFilterCriteria criteria, String keyword, List<Group> result)
            throws ServiceException;

    /**
     * Find all {@code Group} entities omitting filter criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code long} value that represents count of ALL entities (even beyond specified bounds).
     * @throws ServiceException if an error occurred executing the method.
     */
    long filter(int start, int size, List<Group> result) throws ServiceException;

    /**
     * Check if the name is unique.
     *
     * @param name name that needs check.
     * @return {@code true} if the name is unique, {@code false} otherwise.
     * @throws ServiceException if an error occurred executing the method.
     */
    boolean isNameUnique(String name) throws ServiceException;

    /**
     * Create a new {@code Group} entity in the data store.
     *
     * @param group the group to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Group group) throws ServiceException;

    /**
     * Update a {@code Group} entity in the data store.
     *
     * @param group the group to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Group group) throws ServiceException;

    /**
     * Delete a {@code Group} entity with specified id from the data store.
     *
     * @param id unique id of the group to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;
}
