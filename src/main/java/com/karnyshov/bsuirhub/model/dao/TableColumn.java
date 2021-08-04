package com.karnyshov.bsuirhub.model.dao;

public final class TableColumn {
    /*
    * TABLE users
    * */
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

    /*
    * TABLE faculties
    * */
    public static final String FACULTY_ID = "id";
    public static final String FACULTY_NAME = "name";
    public static final String FACULTY_SHORT_NAME = "short_name";
    public static final String FACULTY_IS_ARCHIVED = "is_archived";

    /*
    * TABLE departments
    * */
    public static final String DEPARTMENT_ID = "id";
    public static final String DEPARTMENT_NAME = "name";
    public static final String DEPARTMENT_SHORT_NAME = "short_name";
    public static final String DEPARTMENT_IS_ARCHIVED = "is_archived";
    public static final String DEPARTMENT_FACULTY_ID = "id_faculty";
    public static final String DEPARTMENT_SPECIALTY_ALIAS = "specialty_alias";

    /*
    * TABLE groups
    * */
    public static final String GROUP_ID = "id";
    public static final String GROUP_DEPARTMENT_ID = "id_department";
    public static final String GROUP_HEADMAN_ID = "id_headman";
    public static final String GROUP_CURATOR_ID = "id_curator";
    public static final String GROUP_NAME = "name";

    /*
     * TABLE subjects
     * */
    public static final String SUBJECT_ID = "id";
    public static final String SUBJECT_NAME = "name";
    public static final String SUBJECT_SHORT_NAME = "short_name";
    public static final String SUBJECT_IS_ARCHIVED = "is_archived";

    /*
    * TABLE grades
    * */
    public static final String GRADE_ID = "id";
    public static final String GRADE_VALUE = "value";
    public static final String GRADE_TEACHER_ID = "id_teacher";
    public static final String GRADE_STUDENT_ID = "id_student";
    public static final String GRADE_SUBJECT_ID = "id_subject";
    public static final String GRADE_DATE = "date";
    public static final String GRADE_IS_EXAM = "is_exam";

    /*
    * TABLE comments
    * */
    public static final String COMMENT_ID = "id";
    public static final String COMMENT_GRADE_ID = "id_grade";
    public static final String COMMENT_USER_ID = "id_user";
    public static final String COMMENT_TEXT = "text";

    /*
    * TABLE study_assignments
    * */
    public static final String STUDY_ASSIGNMENT_ID = "id";
    public static final String STUDY_ASSIGNMENT_TEACHER_ID = "id_teacher";
    public static final String STUDY_ASSIGNMENT_SUBJECT_ID = "id_subject";
    public static final String STUDY_ASSIGNMENT_GROUP_ID = "id_group";

    private TableColumn() {

    }
}
