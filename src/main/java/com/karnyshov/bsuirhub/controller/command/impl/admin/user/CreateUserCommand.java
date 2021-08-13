package com.karnyshov.bsuirhub.controller.command.impl.admin.user;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.UserValidator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

@Named
public class CreateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONFIRMED_VALUE = "on";
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "default_profile.jpg";

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String login = request.getParameter(LOGIN);
        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);
        String confirmPassword = request.getParameter(CONFIRM_PASSWORD);
        UserRole role = UserRole.parseRole(request.getParameter(ROLE));
        String firstName = request.getParameter(FIRST_NAME);
        String patronymic = request.getParameter(PATRONYMIC);
        String lastName = request.getParameter(LAST_NAME);
        boolean confirmed = CONFIRMED_VALUE.equals(request.getParameter(CONFIRMED));
        String groupIdString = request.getParameter(GROUP_ID);

        try {
            // when role is not STUDENT, we set groupId to default value
            long groupId = role == UserRole.STUDENT && StringUtils.isNotBlank(groupIdString)
                    ? Long.parseLong(groupIdString)
                    : 0; // default value

            User user = User.builder()
                    .setLogin(login)
                    .setEmail(email)
                    .setRole(role)
                    .setFirstName(firstName)
                    .setPatronymic(patronymic)
                    .setLastName(lastName)
                    .setGroupId(groupId)
                    // empty email -> always not confirmed
                    .setStatus(confirmed && !StringUtils.isBlank(email) ? UserStatus.CONFIRMED : UserStatus.NOT_CONFIRMED)
                    .setProfileImageName(DEFAULT_PROFILE_IMAGE_PATH)
                    .build();

            boolean validationResult = UserValidator.validateUser(user, password, confirmPassword,
                    false, false, false);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            boolean isLoginUnique = userService.isLoginUnique(login);
            boolean isEmailUnique = userService.isEmailUnique(email);

            if (!isLoginUnique) {
                session.setAttribute(NOT_UNIQUE_LOGIN, true);
            }

            if (!isEmailUnique) {
                session.setAttribute(NOT_UNIQUE_EMAIL, true);
            }

            if (validationResult && isLoginUnique && isEmailUnique) {
                // data is valid
                userService.create(user, password);
                result = new CommandResult(ADMIN_USERS_URL, REDIRECT);
            } else {
                // data is not valid
                result = new CommandResult(ADMIN_NEW_USER_URL, REDIRECT);
            }
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'create user' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
