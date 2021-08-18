package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.criteria.AssignmentFilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code AssignmentService} is an interface with a bunch of methods that allow to manipulate {@link Assignment}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface AssignmentService {
    /**
     * Find {@code Assignment} entity by its unique id.
     *
     * @param id unique id of the assignment.
     * @return {@code Assignment} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Assignment> findById(long id) throws ServiceException;

    /**
     * Find {@code Assignment} entities that satisfy certain criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param criteria instance of {@link AssignmentFilterCriteria} that specifies a criteria for filtering.
     * @param filterId stored entities will be filtered by this value.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds) satisfied
     * filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, AssignmentFilterCriteria criteria, long filterId, List<Assignment> result)
            throws ServiceException;

    /**
     * Check if {@code Assignment} entity with specified {@link Group} id and {@link Subject} id exists.
     *
     * @param groupId unique id of the group.
     * @param subjectId unique id of the subject.
     * @return {@code true} if assignment exists, {@code false} otherwise.
     * @throws ServiceException if an error occurred executing the method.
     */
    boolean assignmentExists(long groupId, long subjectId) throws ServiceException;

    /**
     * Check if {@code Assignment} entity with specified {@link Group} id, {@link User Teacher} id
     * and {@link Subject} id exists.
     *
     * @param groupId unique id of the group.
     * @param teacherId unique id of the teacher.
     * @param subjectId unique id of the subject.
     * @return {@code true} if assignment exists, {@code false} otherwise.
     * @throws ServiceException if an error occurred executing the method.
     */
    boolean assignmentExists(long groupId, long teacherId, long subjectId) throws ServiceException;

    /**
     * Create a new {@code Assignment} entity in the data store.
     *
     * @param assignment the assignment to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Assignment assignment) throws ServiceException;

    /**
     * Update an {@code Assignment} entity in the data store.
     *
     * @param assignment the assignment to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Assignment assignment) throws ServiceException;

    /**
     * Delete an {@code Assignment} entity with specified id from the data store.
     *
     * @param id unique id of the assignment to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;
}
