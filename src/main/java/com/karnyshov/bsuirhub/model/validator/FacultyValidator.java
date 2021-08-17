package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Faculty;

import java.util.regex.Pattern;

/**
 * {@code FacultyValidator} class contains methods for validation untrusted data encapsulated in {@link Faculty} entity.
 * @author Dmitry Karnyshov
 */
public class FacultyValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s,-.]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";

    /**
     * Validate {@link Faculty} entity.
     *
     * @param faculty object that needs validation.
     * @return {@code true} if the group is valid, {@code false} otherwise.
     */
    public static boolean validateFaculty(Faculty faculty) {
        boolean validationResult;
        String name = faculty.getName();
        String shortName = faculty.getShortName();

        validationResult = Pattern.matches(VALID_NAME, name);
        validationResult &= Pattern.matches(VALID_SHORT_NAME, shortName);


        return validationResult;
    }
}
