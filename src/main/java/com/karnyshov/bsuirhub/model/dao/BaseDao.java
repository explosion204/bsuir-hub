package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

/**
 * {@code BaseDao} is a top interface in the DAO hierarchy. Contains base data manipulation methods applicable to
 * all subclasses.
 *
 * @param <T> the type parameter extends {@link AbstractEntity} and stands for related to DAO entity.
 * @author Dmitry Karnyshov
 */
public interface BaseDao<T extends AbstractEntity> {
    /**
     * Select all stored entities.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit  amount of records that will appear in the query result.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectAll(int offset, int limit, List<T> result) throws DaoException;

    /**
     * Select total count of all stored entities.
     *
     * @return {@code int} value that represents total count of stored entities.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectTotalCount() throws DaoException;

    /**
     * Select a single entity with specified id.
     *
     * @param id unique id of the entity.
     * @return entity wrapped with {@link Optional}.
     * @throws DaoException if an error occurred while processing the query.
     */
    Optional<T> selectById(long id) throws DaoException;

    /**
     * Insert new entity.
     *
     * @param entity new entity to insert.
     * @return {@code long} value that represents generated id of freshly-inserted entity.
     * @throws DaoException if an error occurred while processing the query.
     */
    long insert(T entity) throws DaoException;

    /**
     * Update existing entity.
     *
     * @param entity object with new data.
     * @return number of affected records.
     * @throws DaoException if an error occurred while processing the query.
     */
    int update(T entity) throws DaoException;

    /**
     * Delete existing (strictly not necessarily) entity.
     *
     * @param id unique id of the entity.
     * @return number of affected records.
     * @throws DaoException if an error occurred while processing the query.
     */
    int delete(long id) throws DaoException;
}
