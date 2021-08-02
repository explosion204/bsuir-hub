package com.karnyshov.bsuirhub.controller.command.validator;

import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;

@Named
public class GroupValidator {
    private static final String VALID_NAME = "^[\\p{L}\\d]{1,20}$";

    @Inject
    private GroupService groupService;

    public boolean validateGroup(HttpServletRequest request, Group group, boolean nameNotChanged) {
        HttpSession session = request.getSession();

        boolean mainValidationResult;
        boolean minorValidationResult;

        String name = group.getName();

        minorValidationResult = Pattern.matches(VALID_NAME, name);
        mainValidationResult = minorValidationResult;
        session.setAttribute(INVALID_NAME, !minorValidationResult);

        minorValidationResult = nameNotChanged || groupService.isNameUnique(name);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(NOT_UNIQUE_NAME, !minorValidationResult);

        return mainValidationResult;
    }
}
