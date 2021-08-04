package com.karnyshov.bsuirhub.controller.command;

public class ApplicationPath {
    /* PURE URLS */
    public static final String INDEX_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String FORGOT_PASSWORD_URL = "/forgot_password";
    public static final String SETTINGS_URL = "/settings";
    public static final String CONFIRM_EMAIL_URL = "/confirm_email?token=";
    public static final String RESET_PASSWORD_URL = "/reset_password?token=";
    public static final String ADMIN_USERS_URL = "/admin/users";
    public static final String ADMIN_NEW_USER_URL = "/admin/users/new";
    public static final String ADMIN_EDIT_USER_URL = "/admin/users/edit?id=";
    public static final String ADMIN_FACULTIES_URL = "/admin/faculties";
    public static final String ADMIN_NEW_FACULTY_URL = "/admin/faculties/new";
    public static final String ADMIN_EDIT_FACULTY_URL = "/admin/faculties/edit?id=";
    public static final String ADMIN_DEPARTMENTS_URL = "/admin/departments";
    public static final String ADMIN_NEW_DEPARTMENT_URL = "/admin/departments/new";
    public static final String ADMIN_EDIT_DEPARTMENT_URL = "/admin/departments/edit?id=";
    public static final String ADMIN_GROUPS_URL = "/admin/groups";
    public static final String ADMIN_NEW_GROUP_URL = "/admin/groups/new";
    public static final String ADMIN_EDIT_GROUP_URL = "/admin/groups/edit?id=";
    public static final String ADMIN_SUBJECTS_URL = "/admin/subjects";
    public static final String ADMIN_NEW_SUBJECT_URL = "/admin/subjects/new";
    public static final String ADMIN_EDIT_SUBJECT_URL = "/admin/subjects/edit?id=";
    public static final String NOT_FOUND_ERROR_URL = "/error/404";
    public static final String INTERNAL_SERVER_ERROR_URL = "/error/500";

    /* JSP for forwarding only */
    public static final String INDEX_JSP = "common/index.jsp";
    public static final String LOGIN_JSP = "common/login.jsp";
    public static final String FORGOT_PASSWORD_JSP = "common/forgot_password.jsp";
    public static final String RESET_PASSWORD_JSP = "common/reset_password.jsp";
    public static final String SETTINGS_JSP = "common/settings.jsp";
    public static final String ERROR_NOT_FOUND_JSP = "common/errors/404.jsp";
    public static final String ERROR_INTERNAL_SERVER_JSP = "common/errors/500.jsp";
    public static final String ADMIN_USERS_JSP = "admin/users/users.jsp";
    public static final String ADMIN_VIEW_USER_JSP = "admin/users/view_user.jsp";
    public static final String ADMIN_FACULTIES_JSP = "admin/faculties/faculties.jsp";
    public static final String ADMIN_VIEW_FACULTY_JSP = "admin/faculties/view_faculty.jsp";
    public static final String ADMIN_DEPARTMENTS_JSP = "admin/departments/departments.jsp";
    public static final String ADMIN_VIEW_DEPARTMENT_JSP = "admin/departments/view_department.jsp";
    public static final String ADMIN_GROUPS_JSP = "/admin/groups/groups.jsp";
    public static final String ADMIN_VIEW_GROUP_JSP = "/admin/groups/view_group.jsp";
    public static final String ADMIN_SUBJECTS_JSP = "/admin/subjects/subjects.jsp";
    public static final String ADMIN_VIEW_SUBJECT_JSP = "/admin/subjects/view_subject.jsp";

    /* DIRECTORIES */
    public static final String PROFILE_PICTURES_ROOT = "static/images/profile";

    private ApplicationPath() {

    }
}
