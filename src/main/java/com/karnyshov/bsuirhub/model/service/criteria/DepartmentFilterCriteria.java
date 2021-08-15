package com.karnyshov.bsuirhub.model.service.criteria;

import com.karnyshov.bsuirhub.model.service.DepartmentService;

/**
 * {@code DepartmentFilterCriteria} enum is used to choose a filter strategy
 * in {@link DepartmentService#filter} method.
 * @author Dmitry Karnyshov
 */
public enum DepartmentFilterCriteria {
    NONE,
    NAME,
    SHORT_NAME,
    FACULTY
}
