package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Subject;

import java.util.regex.Pattern;


public class SubjectValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s,]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";

    public static boolean validateSubject(Subject subject) {
        boolean validationResult;

        String name = subject.getName();
        String shortName = subject.getShortName();

        validationResult = Pattern.matches(VALID_NAME, name);
        validationResult &= Pattern.matches(VALID_SHORT_NAME, shortName);

        return validationResult;
    }
}
