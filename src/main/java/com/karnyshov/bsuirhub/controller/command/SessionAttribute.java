package com.karnyshov.bsuirhub.controller.command;

import jakarta.servlet.http.HttpSession;

/**
 * {@code SessionAttribute} class contains constants keys that are stored as {@link HttpSession} attributes.
 * @author Dmitry Karnyshov
 * */
public final class SessionAttribute {
    public static final String USER = "user";
    public static final String USER_ID = "userId";

    // used for CRUD purposes (detecting that email, role or group name are not changed) in admin panel
    public static final String PREVIOUS_EMAIL = "previousEmail";
    public static final String PREVIOUS_ROLE = "previousRole";
    public static final String PREVIOUS_NAME = "previousName";

    private SessionAttribute() {

    }
}
