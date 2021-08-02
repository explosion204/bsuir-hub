package com.karnyshov.bsuirhub.controller.command.validator;

import com.karnyshov.bsuirhub.model.entity.Subject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.INVALID_NAME;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.INVALID_SHORT_NAME;

@Named
public class SubjectValidator {
    private static final String VALID_NAME = "^(?!\\s)[\\p{L}\\s,]{1,50}(?<!\\s)$";
    private static final String VALID_SHORT_NAME = "^\\p{L}{1,15}$";

    public boolean validateSubject(HttpServletRequest request, Subject subject) {
        HttpSession session = request.getSession();

        boolean mainValidationResult;
        boolean minorValidationResult;

        String name = subject.getName();
        String shortName = subject.getShortName();

        minorValidationResult = Pattern.matches(VALID_NAME, name);
        mainValidationResult = minorValidationResult;
        session.setAttribute(INVALID_NAME, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_SHORT_NAME, shortName);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_SHORT_NAME, !minorValidationResult);

        return mainValidationResult;
    }
}
