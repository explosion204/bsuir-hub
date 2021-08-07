package com.karnyshov.bsuirhub.controller.command;

public class AlertAttribute {
    public static final String AUTH_ERROR = "authError";

    public static final String ENTITY_UPDATE_SUCCESS = "entityUpdateSuccess";
    public static final String EMAIL_CHANGE_SUCCESS = "emailChangeSuccess";
    public static final String PASSWORD_CHANGE_SUCCESS = "passwordChangeSuccess";
    public static final String EMAIL_CONFIRMATION_SUCCESS = "emailConfirmationSuccess";
    public static final String PASSWORD_RESET_LINK_SENT = "passwordResetLinkSent";

    public static final String INVALID_ROLE = "invalidRole";
    public static final String INVALID_LOGIN = "invalidLogin";
    public static final String NOT_UNIQUE_LOGIN = "notUniqueLogin";
    public static final String INVALID_EMAIL = "invalidEmail";
    public static final String NOT_UNIQUE_EMAIL = "notUniqueEmail";
    public static final String INVALID_PASSWORD = "invalidPassword";
    public static final String INVALID_CURRENT_PASSWORD = "invalidCurrentPassword";
    public static final String PASSWORDS_DO_NOT_MATCH = "passwordsDoNotMatch";
    public static final String INVALID_FIRST_NAME = "invalidFirstName";
    public static final String INVALID_PATRONYMIC = "invalidPatronymic";
    public static final String INVALID_LAST_NAME = "invalidLastName";

    public static final String INVALID_NAME = "invalidName";
    public static final String INVALID_SHORT_NAME = "invalidShortName";
    public static final String INVALID_SPECIALTY_ALIAS = "invalidSpecialtyAlias";
    public static final String NOT_UNIQUE_NAME = "notUniqueName";

    private AlertAttribute() {

    }
}
