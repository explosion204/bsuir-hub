package com.karnyshov.bsuirhub.model.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum UserRole {
    GUEST,
    STUDENT,
    TEACHER,
    ADMIN;

    private static final Logger logger = LogManager.getLogger();

    public static UserRole parseRole(int roleId) {
        try {
            return UserRole.values()[roleId];
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("An error occurred trying to parse roleId: " + roleId);
            return UserRole.GUEST;
        }
    }

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
