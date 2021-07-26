package com.karnyshov.bsuirhub.controller.command;

public class SessionAttribute {
    public static final String USER = "user";

    // used for CRUD purposes (detecting email or role changes) in admin panel
    public static final String CACHED_EMAIL = "cached_email";
    public static final String CACHED_ROLE = "cached_role";

    public static final String LOCALE = "locale";

    private SessionAttribute() {

    }
}
