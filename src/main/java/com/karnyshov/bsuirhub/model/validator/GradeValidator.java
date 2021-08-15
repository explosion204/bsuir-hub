package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Grade;

/**
 * {@code GradeValidator} class contains methods for validation untrusted data encapsulated in {@link Grade} entity.
 * @author Dmitry Karnyshov
 */
public class GradeValidator {
    private static final int MIN_GRADE_VALUE = 1;
    private static final int MAX_GRADE_VALUE = 10;

    /**
     * Validate grade value.
     *
     * @param gradeValue grade value that needs validation.
     * @return {@code true} if the grade value is valid, {@code false} otherwise.
     */
    public static boolean validateGradeValue(byte gradeValue) {
        return gradeValue >= MIN_GRADE_VALUE && gradeValue <= MAX_GRADE_VALUE;
    }
}
