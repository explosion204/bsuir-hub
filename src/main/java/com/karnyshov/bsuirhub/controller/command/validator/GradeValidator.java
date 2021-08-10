package com.karnyshov.bsuirhub.controller.command.validator;

import jakarta.inject.Named;


@Named
public class GradeValidator {
    private static final int MAX_GRADE_VALUE = 11;

    public boolean validateGradeValue(byte gradeValue) {
        return gradeValue <= MAX_GRADE_VALUE;
    }
}
