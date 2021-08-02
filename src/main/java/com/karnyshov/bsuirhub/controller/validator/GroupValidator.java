package com.karnyshov.bsuirhub.controller.validator;

import com.karnyshov.bsuirhub.model.entity.Group;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;

@Named
public class GroupValidator {
    private static final String VALID_NAME = "^[\\p{L}\\d]{1,20}$";


    public boolean validateGroup(HttpServletRequest request, Group group) {
        HttpSession session = request.getSession();

        boolean mainValidationResult;
        boolean minorValidationResult;

        String name = group.getName();

        minorValidationResult = Pattern.matches(VALID_NAME, name);
        mainValidationResult = minorValidationResult;
        session.setAttribute(INVALID_NAME, !minorValidationResult);

        return mainValidationResult;
    }
}
