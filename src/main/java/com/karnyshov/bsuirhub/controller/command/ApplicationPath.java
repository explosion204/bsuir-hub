package com.karnyshov.bsuirhub.controller.command;

public class ApplicationPath {
    /* PURE URLS */
    public static final String INDEX_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String ADMIN_USERS_URL = "/admin/users";
    public static final String NOT_FOUND_ERROR_URL = "/error/404";
    public static final String INTERNAL_SERVER_ERROR_URL = "/error/500";

    /* JSP for forwarding only */
    public static final String INDEX_JSP = "common/index.jsp";
    public static final String LOGIN_JSP = "common/login.jsp";
    public static final String ERROR_NOT_FOUND_JSP = "common/errors/404.jsp";
    public static final String ERROR_INTERNAL_SERVER_JSP = "common/errors/500.jsp";
    public static final String ADMIN_USERS_JSP = "admin/users/users.jsp";
    public static final String ADMIN_VIEW_USER_JSP = "admin/users/view_user.jsp";

    /* DIRECTORIES */
    public static final String PROFILE_PICTURES_ROOT = "static/images/profile";

    private ApplicationPath() {

    }
}
