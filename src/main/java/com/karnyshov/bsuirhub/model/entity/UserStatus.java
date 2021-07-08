package com.karnyshov.bsuirhub.model.entity;

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
}
