package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * {@code UserDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link User} entities.
 * @author Dmitry Karnyshov
 */
public interface UserDao extends BaseDao<User> {
    /**
     * Select a single {@code User} with specified login.
     *
     * @param login unique login of the user.
     * @return entity wrapped with {@link Optional}.
     * @throws DaoException if an error occurred while processing the query.
     */
    Optional<User> selectByLogin(String login) throws DaoException;

    /**
     * Select {@code User} entities by specified login.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the login of the user can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByLogin(int offset, int limit, String keyword, List<User> result) throws DaoException;

    /**
     * Select count of {@code User} entities with specified login.
     *
     * @param keyword substring that the login of the user can contain.
     * @return {@code int} value that represents count of stored users with specified login.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByLogin(String keyword) throws DaoException;

    /**
     * Select a single {@code User} with specified email.
     *
     * @param email unique email login of the user.
     * @return entity wrapped with {@link Optional}.
     * @throws DaoException if an error occurred while processing the query.
     */
    Optional<User> selectByEmail(String email) throws DaoException;

    /**
     * Select {@code User} entities by specified email.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the email of the user can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByEmail(int offset, int limit, String keyword, List<User> result) throws DaoException;

    /**
     * Select count of {@code User} entities with specified email.
     *
     * @param keyword substring that the email of the user can contain.
     * @return {@code int} value that represents count of stored users with specified email.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByEmail(String keyword) throws DaoException;

    /**
     * Select {@code User} entities by specified last name.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param keyword substring that the last name of the user can contain.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByLastName(int offset, int limit, String keyword, List<User> result) throws DaoException;

    /**
     * Select count of {@code User} entities with specified last name.
     *
     * @param keyword substring that the last name of the user can contain.
     * @return {@code int} value that represents count of stored users with specified last name.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByLastName(String keyword) throws DaoException;

    /**
     * Select {@code User} entities by specified {@link UserRole} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param roleId unique id of the role.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByRole(int offset, int limit, long roleId, List<User> result) throws DaoException;

    /**
     * Select count of {@code User} entities with specified {@link UserRole} id.
     *
     * @param roleId unique id of the role.
     * @return {@code int} value that represents count of stored users with specified role id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByRole(long roleId) throws DaoException;

    /**
     * Select {@code User} entities by specified {@link Group} id.
     *
     * @param offset amount of records that will be skipped from start in the query result.
     * @param limit amount of records that will appear in the query result.
     * @param groupId unique id of the group.
     * @param result {@link List} instance to hold entities retrieved from database.
     * @throws DaoException if an error occurred while processing the query.
     */
    void selectByGroup(int offset, int limit, long groupId, List<User> result) throws DaoException;

    /**
     * Select count of {@code User} entities with specified {@link Group} id.
     *
     * @param groupId unique id of the group.
     * @return {@code int} value that represents count of stored users with specified group id.
     * @throws DaoException if an error occurred while processing the query.
     */
    int selectCountByGroup(long groupId) throws DaoException;
}
