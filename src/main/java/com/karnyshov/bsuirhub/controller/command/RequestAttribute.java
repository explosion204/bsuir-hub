package com.karnyshov.bsuirhub.controller.command;

import jakarta.servlet.http.HttpServletRequest;

/**
 * {@code RequestAttribute} class contains constant keys that are stored as {@link HttpServletRequest} attributes.
 * @author Dmitry Karnyshov
 * */
public final class RequestAttribute {
    public static final String ORIGINAL_URL = "originalUrl";
    public static final String TARGET_ENTITY = "targetEntity";
    public static final String NEW_ENTITY_PAGE = "newEntityPage";
    public static final String STUDENT = "student";
    public static final String SUBJECT = "subject";
    public static final String AVERAGE_STUDY_GRADE = "averageStudyGrade";
    public static final String GROUP = "group";
    public static final String FACULTY = "faculty";
    public static final String DEPARTMENT = "department";
    public static final String CURATOR = "curator";
    public static final String HEADMAN = "headman";

    private RequestAttribute() {

    }
}
