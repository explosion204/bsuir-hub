package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.listener.AuthenticatedSessionCollector;
import com.karnyshov.bsuirhub.controller.validator.DataValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PROFILE_PICTURE_PATH;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.*;

public class UpdateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONFIRMED_VALUE = "on";

    @Inject
    private UserService userService;

    @Inject
    private DataValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String idString = request.getParameter(ENTITY_ID);
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
        String profilePicturePath = request.getParameter(PROFILE_PICTURE_PATH);

        User user = User.builder()
                .setLogin(login)
                .setEmail(email)
                .setUserRole(role)
                .setFirstName(firstName)
                .setPatronymic(patronymic)
                .setLastName(lastName)
                // empty email -> always not confirmed
                .setUserStatus(confirmed && !StringUtils.isBlank(email) ? UserStatus.CONFIRMED : UserStatus.NOT_CONFIRMED)
                .setProfilePicturePath(profilePicturePath)
                .build();

        if (validator.validateUser(request, user, idString, cachedEmail, cachedRole, password, confirmPassword)) {
            // data is valid
            try {
                long entityId = Long.parseLong(idString);
                User updatedUser = (User) User.builder()
                        .of(user)
                        .setEntityId(entityId)
                        .build();

                userService.update(updatedUser, password);

                // success
                // update target user session if exists
                AuthenticatedSessionCollector.findSession(entityId).ifPresent(
                        targetSession -> targetSession.setAttribute(USER, updatedUser));

                session.setAttribute(SUCCESS, true);
                result = new CommandResult(ADMIN_EDIT_USER_URL + idString, REDIRECT);
            } catch (ServiceException | NumberFormatException e) {
                logger.error("An error occurred executing 'update user' command", e);
                result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
            }
        } else {
            // data is not valid
            result = new CommandResult(ADMIN_EDIT_USER_URL + idString, REDIRECT);
        }

        // clean up cached values
        session.removeAttribute(CACHED_EMAIL);
        session.removeAttribute(CACHED_ROLE);
        return result;
    }
}
