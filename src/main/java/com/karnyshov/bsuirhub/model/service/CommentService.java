package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * {@code CommentService} is an interface with a bunch of methods that allow to manipulate {@link Comment}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface CommentService {
    /**
     * Find {@code Comment} entity by its unique id.
     *
     * @param id unique id of the comment.
     * @return {@code Comment} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<Comment> findById(long id) throws ServiceException;

    /**
     * Find {@code Comment} entities with specified {@link Grade} id.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param gradeId unique id of the grade.
     * @param result {@link List} instance to hold retrieved entities.t
     * @return {@code int} value that represents count of ALL entities satisfied filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    int findByGrade(int start, int size, long gradeId, List<Comment> result) throws ServiceException;

    /**
     * Create a new {@code Comment} entity in the data store.
     *
     * @param comment the comment to create.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(Comment comment) throws ServiceException;

    /**
     * Update a {@code Comment} entity in the data store.
     *
     * @param comment the comment to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(Comment comment) throws ServiceException;

    /**
     * Delete a {@code Comment} entity with specified id from the data store.
     *
     * @param id unique id of the comment to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;
}
