package com.karnyshov.bsuirhub.model.service.criteria;

import com.karnyshov.bsuirhub.model.service.SubjectService;

/**
 * {@code SubjectFilterCriteria} enum is used to choose a filter strategy
 * in {@link SubjectService#filter} method.
 * @author Dmitry Karnyshov
 */
public enum SubjectFilterCriteria {
    NONE,
    NAME,
    SHORT_NAME
}
