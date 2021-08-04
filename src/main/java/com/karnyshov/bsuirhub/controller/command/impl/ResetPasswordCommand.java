package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.UserValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.PASSWORD_CHANGE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.CONFIRM_PASSWORD;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER_ID;

@Named
public class ResetPasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Inject
    private UserValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            Long userId = (Long) request.getSession().getAttribute(USER_ID);
            String password = request.getParameter(PASSWORD);
            String confirmPassword = request.getParameter(CONFIRM_PASSWORD);

            HttpSession session = request.getSession();

            if (userId != null && validator.validatePasswordChange(request, userId, null, password, confirmPassword, true)) {
                // data is valid
                userService.changePassword(userId, password);
                // prevent sending second request
                session.removeAttribute(USER_ID);
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
