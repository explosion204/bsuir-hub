package com.karnyshov.bsuirhub.model.service.criteria;


import com.karnyshov.bsuirhub.model.service.AssignmentService;

/**
 * {@code AssignmentFilterCriteria} enum is used to choose a filter strategy
 * in {@link AssignmentService#filter} method.
 * @author Dmitry Karnyshov
 */
public enum AssignmentFilterCriteria {
    GROUP,
    TEACHER
}
