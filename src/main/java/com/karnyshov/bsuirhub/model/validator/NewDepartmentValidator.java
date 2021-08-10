package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Department;
import java.util.regex.Pattern;


public class NewDepartmentValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s,]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";
    private static final String VALID_SPECIALTY_ALIAS = "^(?!\\s)[\\p{L}\\s,]{1,100}(?<!\\s)$";

    public static boolean validateDepartment(Department department) {
        boolean validationResult;

        String name = department.getName();
        String shortName = department.getShortName();
        String specialtyAlias = department.getSpecialtyAlias(); // TODO: 7/31/2021 FK validation ?

        validationResult = Pattern.matches(VALID_NAME, name);
        validationResult &= Pattern.matches(VALID_SHORT_NAME, shortName);
        validationResult &= Pattern.matches(VALID_SPECIALTY_ALIAS, specialtyAlias);

        return validationResult;
    }
}
