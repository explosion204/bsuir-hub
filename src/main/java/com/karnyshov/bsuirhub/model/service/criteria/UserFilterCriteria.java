package com.karnyshov.bsuirhub.model.service.criteria;

import com.karnyshov.bsuirhub.model.service.UserService;

/**
 * {@code UserFilterCriteria} enum is used to choose a filter strategy
 * in {@link UserService#filter} method.
 * @author Dmitry Karnyshov
 */
public enum UserFilterCriteria {
    NONE,
    LOGIN,
    EMAIL,
    LAST_NAME,
    ROLE,
    GROUP
}
