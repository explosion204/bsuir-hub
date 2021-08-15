package com.karnyshov.bsuirhub.model.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@code UserRole} enum represents a role of user in application.
 * @author Dmitry Karnyshov
 * @see User
 */
public enum UserRole {
    GUEST,
    STUDENT,
    TEACHER,
    ADMIN;

    private static final Logger logger = LogManager.getLogger();


    /**
     * Parse {@code UserRole} instance from integer id.
     *
     * @param roleId unique id tightly coupled with the ordinal value.
     * @return {@code UserRole} instance.
     */
    public static UserRole parseRole(int roleId) {
        try {
            return UserRole.values()[roleId];
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("An error occurred trying to parse roleId: " + roleId);
            return UserRole.GUEST;
        }
    }

    /**
     * Parse {@code UserRole} instance from stringified integer id.
     *
     * @param roleId unique stringified id tightly coupled with the ordinal value.
     * @return {@code UserRole} instance.
     */
    public static UserRole parseRole(String roleId) {
        try {
            return parseRole(Integer.parseInt(roleId));
        } catch (NumberFormatException e) {
            logger.error("An error occurred trying to parse roleId: " + roleId);
            return UserRole.GUEST;
        }
    }

    @Override
    public String toString() {
        return name() + "/" + ordinal();
    }
}
