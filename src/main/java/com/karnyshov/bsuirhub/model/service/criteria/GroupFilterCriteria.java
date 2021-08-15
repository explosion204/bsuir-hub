package com.karnyshov.bsuirhub.model.service.criteria;

import com.karnyshov.bsuirhub.model.service.GroupService;

/**
 * {@code GroupFilterCriteria} enum is used to choose a filter strategy
 * in {@link GroupService#filter} method.
 * @author Dmitry Karnyshov
 */
public enum GroupFilterCriteria {
    NONE,
    NAME,
    DEPARTMENT
}
