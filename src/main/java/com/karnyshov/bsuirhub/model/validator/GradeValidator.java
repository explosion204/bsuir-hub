package com.karnyshov.bsuirhub.model.validator;

public class GradeValidator {
    private static final int MIN_GRADE_VALUE = 1;
    private static final int MAX_GRADE_VALUE = 10;

    public static boolean validateGradeValue(byte gradeValue) {
        return gradeValue >= MIN_GRADE_VALUE && gradeValue <= MAX_GRADE_VALUE;
    }
}
