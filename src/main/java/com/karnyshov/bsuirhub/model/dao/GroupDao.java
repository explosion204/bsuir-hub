package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.List;
import java.util.Optional;

/**
 * {@code GroupDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Group} entities.
 * @author Dmitry Karnyshov
 */
public interface GroupDao extends BaseDao<Group> {
    /**
     * Select a single {@code Group} with specified name.
     *
     * @param name unique name of the group.
     * @return entity wrapped with {@link Optional}.
     * @throws DaoException if an error occurred while processing the query.
     */
    Optional<Group> selectByName(String name) throws DaoException;

    /**
     * Select {@code Group} entities by specified name.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the name of the group can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByName(int offset, int limit, String keyword, List<Group> result) throws DaoException;

    /**
     * Select count of {@code Group} entities with specified name.
     *
     * @param keyword substring that the name of the group can contain.
     * @return {@code int} value that represents count of stored groups with specified name.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByName(String keyword) throws DaoException;

    /**
     * Select {@code Group} entities by specified {@link Department} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param departmentId unique id of the department.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByDepartment(int offset, int limit, long departmentId, List<Group> result)
            throws DaoException;

    /**
     * Select count of {@code Group} entities with specified {@link Department} id.
     *
     * @param departmentId unique id of the department.
     * @return {@code int} value that represents count of stored groups with specified department id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByDepartment(long departmentId) throws DaoException;
}
