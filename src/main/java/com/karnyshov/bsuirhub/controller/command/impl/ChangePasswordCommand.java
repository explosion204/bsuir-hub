package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.UserValidator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.SETTINGS_URL;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.ERROR;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * {@code ChangePasswordCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class ChangePasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService;

    @Inject
    public ChangePasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        try {
            User user = (User) request.getSession().getAttribute(USER);
            long targetId = user.getEntityId();

            String currentPassword = request.getParameter(CURRENT_PASSWORD);
            String password = request.getParameter(PASSWORD);
            String confirmPassword = request.getParameter(CONFIRM_PASSWORD);

            boolean validationResult = UserValidator.validatePassword(password, confirmPassword);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            Optional<User> targetUser = userService.findById(targetId);
            if (targetUser.isPresent()) {
                String currentPasswordHash = targetUser.get().getPasswordHash();
                String currentSalt = targetUser.get().getSalt();

                validationResult &= StringUtils.equals(currentPasswordHash,
                        DigestUtils.sha256Hex(currentPassword + currentSalt));
                session.setAttribute(INVALID_CURRENT_PASSWORD, !validationResult);
            }

            if (validationResult) {
                // data is valid
                userService.changePassword(targetId, password);
                session.setAttribute(PASSWORD_CHANGE_SUCCESS, true);
            }

            result = new CommandResult(SETTINGS_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'change password' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
