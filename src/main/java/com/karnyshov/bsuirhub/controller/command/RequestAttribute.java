package com.karnyshov.bsuirhub.controller.command;

public class RequestAttribute {
    public static final String ORIGINAL_URL = "original_url";
    public static final String AUTH_ERROR = "auth_error";
    public static final String INVALID_ROLE = "invalid_role";
    public static final String INVALID_LOGIN = "invalid_login";
    public static final String NOT_UNIQUE_LOGIN = "not_unique_login";
    public static final String INVALID_EMAIL = "invalid_email";
    public static final String NOT_UNIQUE_EMAIL = "not_unique_email";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String PASSWORDS_DO_NOT_MATCH = "passwords_do_not_match";
    public static final String INVALID_FIRST_NAME = "invalid_first_name";
    public static final String INVALID_PATRONYMIC = "invalid_patronymic";
    public static final String INVALID_LAST_NAME = "invalid_last_name";

    public static final String TARGET_USER = "target_user";

    public static final String NEW_ENTITY_PAGE = "new_entity_page";
    public static final String ENTITY_ID = "id";

    private RequestAttribute() {

    }
}
