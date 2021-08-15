package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.entity.Subject;

import java.util.regex.Pattern;

/**
 * {@code DepartmentValidator} class contains methods for validation untrusted data encapsulated in {@link Department}
 * entity.
 * @author Dmitry Karnyshov
 */
public class DepartmentValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s,]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";
    private static final String VALID_SPECIALTY_ALIAS = "^(?!\\s)[\\p{L}\\s,]{1,100}(?<!\\s)$";

    /**
     * Validate {@link Department} entity.
     *
     * @param department object that needs validation.
     * @return {@code true} if the department is valid, {@code false} otherwise.
     */
    public static boolean validateDepartment(Department department) {
        boolean validationResult;

        String name = department.getName();
        String shortName = department.getShortName();
        String specialtyAlias = department.getSpecialtyAlias();

        validationResult = Pattern.matches(VALID_NAME, name);
        validationResult &= Pattern.matches(VALID_SHORT_NAME, shortName);
        validationResult &= Pattern.matches(VALID_SPECIALTY_ALIAS, specialtyAlias);

        return validationResult;
    }
}
