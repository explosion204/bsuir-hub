package com.karnyshov.bsuirhub.controller.command;

public class ApplicationPath {
    /* PURE URLS */
    public static final String INDEX_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String FORGOT_PASSWORD_URL = "/login/forgot_password";
    public static final String SETTINGS_URL = "/settings";
    public static final String CHANGE_PASSWORD_URL = "/settings/change_password";
    public static final String CHANGE_EMAIL_URL = "/settings/change_email";
    public static final String TEACHER_DASHBOARD_URL = "/teacher";
    public static final String STUDENT_DASHBOARD_URL = "/student";
    public static final String GRADES_OVERVIEW_URL = "/grades";
    public static final String CONFIRM_EMAIL_URL = "/settings/confirm_email";
    public static final String SEND_RESET_PASSWORD_LINK_URL = "/login/send_reset_password_link";
    public static final String RESET_PASSWORD_URL = "/login/reset_password";
    public static final String ADMIN_USERS_URL = "/admin/users";
    public static final String ADMIN_NEW_USER_URL = "/admin/users/new";
    public static final String ADMIN_EDIT_USER_URL = "/admin/users/edit";
    public static final String ADMIN_DELETE_USER_URL = "/admin/users/delete";
    public static final String ADMIN_FACULTIES_URL = "/admin/faculties";
    public static final String ADMIN_NEW_FACULTY_URL = "/admin/faculties/new";
    public static final String ADMIN_EDIT_FACULTY_URL = "/admin/faculties/edit";
    public static final String ADMIN_DELETE_FACULTY_URL = "/admin/faculties/delete";
    public static final String ADMIN_DEPARTMENTS_URL = "/admin/departments";
    public static final String ADMIN_NEW_DEPARTMENT_URL = "/admin/departments/new";
    public static final String ADMIN_EDIT_DEPARTMENT_URL = "/admin/departments/edit";
    public static final String ADMIN_DELETE_DEPARTMENT_URL = "/admin/departments/delete";
    public static final String ADMIN_GROUPS_URL = "/admin/groups";
    public static final String ADMIN_NEW_GROUP_URL = "/admin/groups/new";
    public static final String ADMIN_EDIT_GROUP_URL = "/admin/groups/edit";
    public static final String ADMIN_DELETE_GROUP_URL = "/admin/groups/delete";
    public static final String ADMIN_SUBJECTS_URL = "/admin/subjects";
    public static final String ADMIN_NEW_SUBJECT_URL = "/admin/subjects/new";
    public static final String ADMIN_EDIT_SUBJECT_URL = "/admin/subjects/edit";
    public static final String ADMIN_DELETE_SUBJECT_URL = "/admin/subjects/delete";
    public static final String NOT_FOUND_ERROR_URL = "/error/404";
    public static final String INTERNAL_SERVER_ERROR_URL = "/error/500";
    public static final String AJAX_SET_LOCALE_URL = "/ajax/set_locale";
    public static final String AJAX_GET_USERS_URL = "/ajax/get_users";
    public static final String AJAX_GET_FACULTIES_URL = "/ajax/get_faculties";
    public static final String AJAX_GET_DEPARTMENTS_URL = "/ajax/get_departments";
    public static final String AJAX_GET_GROUPS_URL = "/ajax/get_groups";
    public static final String AJAX_GET_SUBJECTS_URL = "/ajax/get_subjects";
    public static final String AJAX_GET_ASSIGNMENTS_URL = "/ajax/get_assignments";
    public static final String AJAX_GET_GRADES_URL = "/ajax/get_grades";
    public static final String AJAX_GET_AVERAGE_GRADE_URL = "/ajax/get_average_grade";
    public static final String AJAX_UPLOAD_PROFILE_IMAGE_URL = "/ajax/upload_profile_image";
    public static final String AJAX_CREATE_GRADE_URL = "/grades/ajax/create_grade";
    public static final String AJAX_UPDATE_GRADE_URL = "/grades/ajax/update_grade";
    public static final String AJAX_DELETE_GRADE_URL = "/grades/ajax/delete_grade";
    public static final String ADMIN_AJAX_CREATE_ASSIGNMENT_URL = "/admin/ajax/create_assignment";
    public static final String ADMIN_AJAX_UPDATE_ASSIGNMENT_URL = "/admin/ajax/update_assignment";
    public static final String ADMIN_AJAX_DELETE_ASSIGNMENT_URL = "/admin/ajax/delete_assignment";

    /* JSP for forwarding only */
    public static final String INDEX_JSP = "common/index.jsp";
    public static final String LOGIN_JSP = "common/login.jsp";
    public static final String FORGOT_PASSWORD_JSP = "common/forgot_password.jsp";
    public static final String RESET_PASSWORD_JSP = "common/reset_password.jsp";
    public static final String SETTINGS_JSP = "common/settings.jsp";
    public static final String TEACHER_DASHBOARD_JSP = "common/teacher_dashboard.jsp";
    public static final String STUDENT_DASHBOARD_JSP = "common/student_dashboard.jsp";
    public static final String GRADES_OVERVIEW_JSP = "common/grades_overview.jsp";
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
