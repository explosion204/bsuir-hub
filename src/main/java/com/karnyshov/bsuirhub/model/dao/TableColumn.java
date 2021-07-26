package com.karnyshov.bsuirhub.model.dao;

public final class TableColumn {
    // TODO: 6/17/2021 table names doc
    public static final String USER_ID = "id";
    public static final String USER_LOGIN = "login";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD_HASH = "password_hash";
    public static final String USER_SALT = "salt";
    public static final String USER_ROLE_ID = "id_role";
    public static final String USER_STATUS_ID = "id_status";
    public static final String USER_GROUP_ID = "id_group";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_PATRONYMIC = "patronymic";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_PROFILE_PICTURE = "profile_picture";

    public static final String DEPARTMENT_ID = "id";
    public static final String DEPARTMENT_NAME = "name";
    public static final String DEPARTMENT_SHORT_NAME = "short_name";
    public static final String DEPARTMENT_IS_ARCHIVED = "is_archived";
    public static final String DEPARTMENT_FACULTY_ID = "id_faculty";
    public static final String DEPARTMENT_SPECIALTY_ALIAS = "specialty_alias";

    private TableColumn() {

    }
}
