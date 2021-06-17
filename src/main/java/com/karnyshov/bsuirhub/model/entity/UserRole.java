package com.karnyshov.bsuirhub.model.entity;

public enum UserRole {
    GUEST(0),
    STUDENT(1),
    TEACHER(2),
    ADMIN(3);

    private final long roleId;

    UserRole(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }
}
