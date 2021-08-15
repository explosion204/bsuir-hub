package com.karnyshov.bsuirhub.model.service.criteria;

import com.karnyshov.bsuirhub.model.service.FacultyService;

/**
 * {@code FacultyFilterCriteria} enum is used to choose a filter strategy
 * in {@link FacultyService#filter} method.
 * @author Dmitry Karnyshov
 */
public enum FacultyFilterCriteria {
    NONE,
    NAME,
    SHORT_NAME
}
