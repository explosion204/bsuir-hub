package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code DepartmentService} is an interface with a bunch of methods that allow to manipulate {@link Department}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface DepartmentService {
    /**
     * Find {@code Department} entity by its unique id.
     *
     * @param id unique id of the department.
     * @return {@code Department} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Department> findById(long id) throws ServiceException;

    /**
     * Find {@code Department} entities that satisfy certain criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param criteria instance of {@link DepartmentFilterCriteria} that specifies a criteria for filtering.
     * @param keyword stored entities will be filtered by this value.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds) satisfied
     * filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, DepartmentFilterCriteria criteria, String keyword, List<Department> result)
            throws ServiceException;

    /**
     * Find all {@code Department} entities omitting filter criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds).
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, List<Department> result) throws ServiceException;

    /**
     * Create a new {@code Department} entity in the data store.
     *
     * @param department the department to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Department department) throws ServiceException;

    /**
     * Update a {@code Department} entity in the data store.
     *
     * @param department the department to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Department department) throws ServiceException;

    /**
     * Delete a {@code Department} entity with specified id from the data store.
     *
     * @param id unique id of the department to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;

}
