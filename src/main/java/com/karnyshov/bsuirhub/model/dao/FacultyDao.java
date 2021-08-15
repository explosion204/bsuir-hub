package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Faculty;

import java.util.List;

/**
 * {@code FacultyDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Faculty} entities.
 * @author Dmitry Karnyshov
 */
public interface FacultyDao extends BaseDao<Faculty> {
    /**
     * Select {@code Faculty} entities by specified name.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the name of the faculty can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException;

    /**
     * Select count of {@code Faculty} entities with specified name.
     *
     * @param keyword substring that the name of the faculty can contain.
     * @return {@code long} value that represents count of stored faculties with specified name.
     * @throws DaoException if an error occurred while processing the query.
     */
    long selectCountByName(String keyword) throws DaoException;

    /**
     * Select {@code Faculty} entities by specified short name.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the short name of the faculty can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByShortName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException;

    /**
     * Select count of {@code Faculty} entities with specified short name.
     *
     * @param keyword substring that the short name of the faculty can contain.
     * @return {@code long} value that represents count of stored faculties with specified short name.
     * @throws DaoException if an error occurred while processing the query.
     */
    long selectCountByShortName(String keyword) throws DaoException;
}
