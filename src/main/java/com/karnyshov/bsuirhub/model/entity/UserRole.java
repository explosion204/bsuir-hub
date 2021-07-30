package com.karnyshov.bsuirhub.model.entity;

import java.util.Arrays;

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

    public static UserRole parseRole(long roleId) {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.roleId == roleId)
                .findFirst()
                .orElse(GUEST);
    }

    public static UserRole parseRole(String roleId) {
        return parseRole(Long.parseLong(roleId));
    }

    @Override
    public String toString() {
        return name() + "/" + roleId;
    }
}
