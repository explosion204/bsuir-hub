package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.*;
import com.karnyshov.bsuirhub.controller.listener.AuthenticatedSessionCollector;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.UserDataValidator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.*;

public class SaveUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONFIRMED_VALUE = "on";
    private static final String EMPTY_EMAIL = "";
    private static final String UNDERSCORE = "_";
    private static final int FILE_UNIQUE_SEQUENCE_LENGTH = 8;
    private static final String IMAGE_MIME_TYPE = "image/";
    private static final String IMAGE_FILE_EXTENSION = ".jpg";

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        // idString == null -> create operation
        // idString != null -> update operation
        String idString = request.getParameter(RequestParameter.ENTITY_ID);
        String login = request.getParameter(LOGIN);
        String email = request.getParameter(EMAIL);
        String cachedEmail = (String) session.getAttribute(CACHED_EMAIL);
        String password = request.getParameter(PASSWORD);
        String confirmPassword = request.getParameter(CONFIRM_PASSWORD);
        UserRole role = UserRole.parseRole(request.getParameter(ROLE));
        UserRole cachedRole = (UserRole) session.getAttribute(CACHED_ROLE);
        String firstName = request.getParameter(FIRST_NAME);
        String patronymic = request.getParameter(PATRONYMIC);
        String lastName = request.getParameter(LAST_NAME);
        boolean confirmed = CONFIRMED_VALUE.equals(request.getParameter(CONFIRMED));

        User user = User.builder()
                .setLogin(login)
                .setEmail(email)
                .setUserRole(role)
                .setFirstName(firstName)
                .setPatronymic(patronymic)
                .setLastName(lastName)
                // empty email -> always not confirmed
                .setUserStatus(confirmed && !StringUtils.isBlank(email) ? UserStatus.CONFIRMED : UserStatus.NOT_CONFIRMED)
                .build();

        if (performValidation(request, user, idString, cachedEmail, cachedRole, password, confirmPassword)) {
            // data is valid
            try {
                long entityId;
                if (idString != null) {
                    // update user
                    entityId = Long.parseLong(idString);
                    user = (User) User.builder()
                            .of(user)
                            .setEntityId(entityId)
                            .build();

                    userService.update(user, password);
                } else {
                    // create user
                    entityId = userService.create(user, password);
                }

                // success
                // update target user session if exists
                User updatedUser = user; // effectively final variable
                AuthenticatedSessionCollector.findSession(entityId).ifPresent(
                        targetSession -> targetSession.setAttribute(USER, updatedUser));

                result = new CommandResult(ADMIN_USERS_URL, REDIRECT);
            } catch (ServiceException | NumberFormatException e) {
                logger.error("An error occurred executing login command", e);
                result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
            }
        } else {
            // data is not valid
            request.setAttribute(TARGET_USER, user);
            request.setAttribute(RequestAttribute.ENTITY_ID, idString);
            result = new CommandResult(ADMIN_VIEW_USER_JSP, FORWARD);
        }

        // clean up cached values
        session.removeAttribute(CACHED_EMAIL);
        session.removeAttribute(CACHED_ROLE);
        return result;
    }

    // main server-side user validation that calls minor validation methods
    private boolean performValidation(HttpServletRequest request, User user, String idString, String cachedEmail,
                UserRole cachedRole, String password, String confirmPassword) {
        boolean mainValidationResult = true;
        boolean minorValidationResult;
        UserRole role = user.getUserRole();
        String login = user.getLogin();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();
        String lastName = user.getLastName();

        // this makes impossible to grant administrator role without direct access to database
        minorValidationResult = role == cachedRole || role != UserRole.ADMIN;
        mainValidationResult = minorValidationResult;
        request.setAttribute(INVALID_ROLE, !minorValidationResult);

        minorValidationResult = UserDataValidator.validateLogin(login);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(INVALID_LOGIN, !minorValidationResult);

        // update user -> ignore login uniqueness validation because dao ignores login anyways
        minorValidationResult = idString != null || userService.isLoginUnique(login);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(NOT_UNIQUE_LOGIN, !minorValidationResult);

        minorValidationResult = StringUtils.isBlank(email) || UserDataValidator.validateEmail(email);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(INVALID_EMAIL, !minorValidationResult);

        // update user -> ignore email uniqueness validation if email is not changed or empty
        minorValidationResult = idString != null && Objects.equals(email, cachedEmail) || StringUtils.isBlank(email)
                || userService.isEmailUnique(email);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(NOT_UNIQUE_EMAIL, !minorValidationResult);

        // password validation is ignored (case - update user leaving password fields empty)
        minorValidationResult = idString != null && StringUtils.isBlank(password)
                || UserDataValidator.validatePassword(password);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(INVALID_PASSWORD, !minorValidationResult);

        minorValidationResult = idString != null && StringUtils.isBlank(password)
                || StringUtils.equals(password, confirmPassword);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(PASSWORDS_DO_NOT_MATCH, !minorValidationResult);

        minorValidationResult = UserDataValidator.validateName(firstName);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(INVALID_FIRST_NAME, !minorValidationResult);

        minorValidationResult = UserDataValidator.validateName(patronymic);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(INVALID_PATRONYMIC, !minorValidationResult);

        minorValidationResult = UserDataValidator.validateName(lastName);
        mainValidationResult &= minorValidationResult;
        request.setAttribute(INVALID_LAST_NAME, !minorValidationResult);

        return mainValidationResult;
    }
}
