package com.karnyshov.bsuirhub.controller.command.impl.admin;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.listener.AuthenticatedSessionCollector;
import com.karnyshov.bsuirhub.controller.validator.UserValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PROFILE_PICTURE_PATH;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.*;

@Named
public class UpdateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONFIRMED_VALUE = "on";
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "default_profile.jpg";

    @Inject
    private UserService userService;

    @Inject
    private UserValidator validator;

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
                .setRole(role)
                .setFirstName(firstName)
                .setPatronymic(patronymic)
                .setLastName(lastName)
                // empty email -> always not confirmed
                .setStatus(confirmed && StringUtils.isNotBlank(email) ? UserStatus.CONFIRMED : UserStatus.NOT_CONFIRMED)
                .setProfilePicturePath(StringUtils.isNotBlank(profilePicturePath) ? profilePicturePath : DEFAULT_PROFILE_IMAGE_PATH)
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
                // this command can be executed only by administrator, so we have to update session this way
                // because it belongs to another user
                AuthenticatedSessionCollector.findSession(entityId).ifPresent(
                        targetSession -> targetSession.setAttribute(USER, updatedUser));

                session.setAttribute(ENTITY_UPDATE_SUCCESS, true);
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
