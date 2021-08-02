package com.karnyshov.bsuirhub.controller.command;

public class SessionAttribute {
    public static final String USER = "user";

    // used for CRUD purposes (detecting that email, role or group name are not changed) in admin panel
    public static final String PREVIOUS_EMAIL = "previous_email";
    public static final String PREVIOUS_ROLE = "previous_role";
    public static final String PREVIOUS_NAME = "previous_name";

    public static final String LOCALE = "locale";

    private SessionAttribute() {

    }
}
