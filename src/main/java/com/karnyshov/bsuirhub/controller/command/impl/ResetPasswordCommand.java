package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.UserValidator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.PASSWORD_CHANGE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.CONFIRM_PASSWORD;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER_ID;

/**
 * {@code ResetPasswordCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class ResetPasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService;

    @Inject
    public ResetPasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        try {
            Long userId = (Long) request.getSession().getAttribute(USER_ID);
            String password = request.getParameter(PASSWORD);
            String confirmPassword = request.getParameter(CONFIRM_PASSWORD);

            boolean validationResult = UserValidator.validatePassword(password, confirmPassword);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            if (validationResult) {
                // data is valid
                // prevent sending second request
                session.removeAttribute(USER_ID);

                userService.changePassword(userId, password);
                session.setAttribute(PASSWORD_CHANGE_SUCCESS, true);
            }

            result = new CommandResult(LOGIN_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'reset password' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
