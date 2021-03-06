package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Faculty;

import java.util.List;

/**
 * {@code DepartmentDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Department} entities.
 * @author Dmitry Karnyshov
 */
public interface DepartmentDao extends BaseDao<Department> {
    /**
     * Select {@code Department} entities by specified name.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the name of the department can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByName(int offset, int limit, String keyword, List<Department> result) throws DaoException;

    /**
     * Select count of {@code Department} entities with specified name.
     *
     * @param keyword substring that the name of the department can contain.
     * @return {@code int} value that represents count of stored departments with specified name.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByName(String keyword) throws DaoException;

    /**
     * Select {@code Department} entities by specified short name.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the short name of the department can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByShortName(int offset, int limit, String keyword, List<Department> result) throws DaoException;

    /**
     * Select count of {@code Department} entities with specified short name.
     *
     * @param keyword substring that the short name of the department can contain.
     * @return {@code int} value that represents count of stored departments with specified short name.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByShortName(String keyword) throws DaoException;

    /**
     * Select {@code Department} entities by specified {@link Faculty} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param facultyId unique id of the faculty.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByFaculty(int offset, int limit, long facultyId, List<Department> result) throws DaoException;

    /**
     * Select count of {@code Department} entities with specified {@link Faculty} id.
     *
     * @param facultyId unique id of the faculty.
     * @return {@code int} value that represents count of stored departments with specified faculty id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByFaculty(long facultyId) throws DaoException;
}
