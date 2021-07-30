package com.karnyshov.bsuirhub.model.entity;

import java.util.Arrays;

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
