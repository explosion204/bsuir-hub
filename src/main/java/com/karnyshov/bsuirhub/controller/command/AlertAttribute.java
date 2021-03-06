package com.karnyshov.bsuirhub.controller.command;

import jakarta.servlet.http.HttpSession;

/**
 * {@code AlertAttribute} class contains alert constant keys that are temporarily stored in {@link HttpSession}.
 * @author Dmitry Karnyshov
 * */
public final class AlertAttribute {
    public static final String AUTH_ERROR = "authError";

    public static final String ENTITY_UPDATE_SUCCESS = "entityUpdateSuccess";
    public static final String EMAIL_CHANGE_SUCCESS = "emailChangeSuccess";
    public static final String PASSWORD_CHANGE_SUCCESS = "passwordChangeSuccess";
    public static final String EMAIL_CONFIRMATION_SUCCESS = "emailConfirmationSuccess";
    public static final String PASSWORD_RESET_LINK_SENT = "passwordResetLinkSent";
    public static final String MESSAGE_SENT = "messageSent";

    public static final String VALIDATION_ERROR = "validationError";

    public static final String NOT_UNIQUE_LOGIN = "notUniqueLogin";
    public static final String NOT_UNIQUE_EMAIL = "notUniqueEmail";
    public static final String INVALID_CURRENT_PASSWORD = "invalidCurrentPassword";
    public static final String NOT_UNIQUE_NAME = "notUniqueName";

    private AlertAttribute() {

    }
}
