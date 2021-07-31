package com.karnyshov.bsuirhub.controller.validator;

import com.karnyshov.bsuirhub.model.entity.Department;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;

@Named
public class DepartmentValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";
    private static final String VALID_SPECIALTY_ALIAS = "^(?!\\s)[\\p{L}\\s,]{1,100}(?<!\\s)$";

    public boolean validateDepartment(HttpServletRequest request, Department department) {
        HttpSession session = request.getSession();

        boolean mainValidationResult;
        boolean minorValidationResult;

        String name = department.getName();
        String shortName = department.getShortName();
        String specialtyAlias = department.getSpecialtyAlias(); // TODO: 7/31/2021 FK validation ?

        minorValidationResult = Pattern.matches(VALID_NAME, name);
        mainValidationResult = minorValidationResult;
        session.setAttribute(INVALID_NAME, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_SHORT_NAME, shortName);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_SHORT_NAME, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_SPECIALTY_ALIAS, specialtyAlias);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_SPECIALTY_ALIAS, !minorValidationResult);

        return mainValidationResult;
    }
}
