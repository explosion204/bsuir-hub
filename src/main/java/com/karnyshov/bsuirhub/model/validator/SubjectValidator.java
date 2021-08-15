package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.entity.User;

import java.util.regex.Pattern;


/**
 * {@code SubjectValidator} class contains methods for validation untrusted data encapsulated in {@link Subject} entity.
 * @author Dmitry Karnyshov
 */
public class SubjectValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s,]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";

    /**
     * Validate {@link Subject} entity.
     *
     * @param subject object that needs validation.
     * @return {@code true} if the subject is valid, {@code false} otherwise.
     */
    public static boolean validateSubject(Subject subject) {
        boolean validationResult;

        String name = subject.getName();
        String shortName = subject.getShortName();

        validationResult = Pattern.matches(VALID_NAME, name);
        validationResult &= Pattern.matches(VALID_SHORT_NAME, shortName);

        return validationResult;
    }
}
