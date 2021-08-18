package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.criteria.UserFilterCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code UserService} is an interface with a bunch of methods that allow to manipulate {@link User}
 * entities without binding to concrete database.
 * @author Dmitry Karnyshov
 */
public interface UserService {
    /**
     * Authenticate user with specified credentials.
     *
     * @param login plain text login.
     * @param password plain text password.
     * @return {@code User} entity wrapped with {@link Optional}. If credentials are invalid, {@code Optional}
     * will be empty.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<User> authenticate(String login, String password) throws ServiceException;

    /**
     * Find {@code User} entity by its unique id.
     *
     * @param id unique id of the user.
     * @return {@code User} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<User> findById(long id) throws ServiceException;

    /**
     * Find {@code User} entity by its unique email.
     *
     * @param email unique email of the user.
     * @return {@code User} entity wrapped with {@link Optional}.
     * @throws ServiceException if an error occurred executing the method.
     */
    Optional<User> findByEmail(String email) throws ServiceException;

    /**
     * Find {@code User} entities that satisfy certain criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param criteria instance of {@link UserFilterCriteria} that specifies a criteria for filtering.
     * @param keyword stored entities will be filtered by this value.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds) satisfied
     * filter criteria and filter value.
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, UserFilterCriteria criteria, String keyword, List<User> result)
            throws ServiceException;

    /**
     * Find all {@code User} entities omitting filter criteria.
     *
     * @param start lower bound index from which the result collection will start.
     * @param size size of the result collection.
     * @param result {@link List} instance to hold retrieved entities.
     * @return {@code int} value that represents count of ALL entities (even beyond specified bounds).
     * @throws ServiceException if an error occurred executing the method.
     */
    int filter(int start, int size, List<User> result) throws ServiceException;

    /**
     * Hash password.
     *
     * @param password plain text password.
     * @return hashed password.
     */
    String hashPassword(String password);

    /**
     * Change password of the user.
     *
     * @param id unique id of the user.
     * @param newPassword new password.
     * @return instance of updated {@link User}.
     * @throws ServiceException if an error occurred executing the method.
     */
    User changePassword(long id, String newPassword) throws ServiceException;

    /**
     * Change email of the user.
     *
     * @param id unique id of ther user.
     * @param newEmail new email.
     * @return instance of updated {@link User}.
     * @throws ServiceException if an error occurred executing the method.
     */
    User changeEmail(long id, String newEmail) throws ServiceException;

    /**
     * Check if the login is unique.
     *
     * @param login login that needs check.
     * @return {@code true} if the login is unique, {@code false} otherwise.
     * @throws ServiceException if an error occurred executing the method.
     */
    boolean isLoginUnique(String login) throws ServiceException;

    /**
     * Check if the email is unique.
     *
     * @param email email that needs check.
     * @return {@code true} if the email is unique, {@code false} otherwise.
     * @throws ServiceException if an error occurred executing the method.
     */
    boolean isEmailUnique(String email) throws ServiceException;

    /**
     * Create a new {@code User} entity in the data store.
     *
     * @param user the user to create.
     * @param password plain text password of the user.
     * @return {@code long} value that represents id of created entity.
     * @throws ServiceException if an error occurred executing the method.
     */
    long create(User user, String password) throws ServiceException;

    /**
     * Update a {@code User} entity in the data store.
     *
     * @param user the user to update.
     * @param password plain text password of the user.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(User user, String password) throws ServiceException;

    /**
     * Update a {@code User} entity in the data store without password changing.
     *
     * @param user the user to update.
     * @throws ServiceException if an error occurred executing the method.
     */
    void update(User user) throws ServiceException;

    /**
     * Delete a {@code User} entity with specified id from the data store.
     *
     * @param id unique id of the user to delete.
     * @throws ServiceException if an error occurred executing the method.
     */
    void delete(long id) throws ServiceException;
}
