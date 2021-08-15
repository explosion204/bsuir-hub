package com.karnyshov.bsuirhub.model.entity;

import java.util.Arrays;


/**
 * {@code UserStatus} enum represents a status of user in application.
 * @author Dmitry Karnyshov
 * @see User
 */
public enum UserStatus {
    NOT_CONFIRMED(1),
    CONFIRMED(2),
    DELETED(3);

    private final long statusId;

    UserStatus(long statusId) {
        this.statusId = statusId;
    }

    public long getStatusId() {
        return statusId;
    }

    /**
     * Parse {@code UserStatus} instance from integer id.
     *
     * @param statusId unique id.
     * @return {@code UserStatus} instance.
     */
    public static UserStatus parseStatus(long statusId) {
        return Arrays.stream(UserStatus.values())
                .filter(status -> status.statusId == statusId)
                .findFirst()
                .orElse(NOT_CONFIRMED);
    }

    @Override
    public String toString() {
        return name() + "/" + statusId;
    }
}
