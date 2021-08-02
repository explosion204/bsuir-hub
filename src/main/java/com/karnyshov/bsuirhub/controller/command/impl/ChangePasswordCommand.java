package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.UserValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.PASSWORD_CHANGE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INTERNAL_SERVER_ERROR_URL;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.SETTINGS_URL;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

@Named
public class ChangePasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Inject
    private UserValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            User user = (User) request.getSession().getAttribute(USER);
            long targetId = user.getEntityId();

            String currentPassword = request.getParameter(CURRENT_PASSWORD);
            String password = request.getParameter(PASSWORD);
            String confirmPassword = request.getParameter(CONFIRM_PASSWORD);

            if (validator.validatePasswordChange(request, targetId, currentPassword, password, confirmPassword)) {
                // data is valid
                userService.changePassword(targetId, password);
                request.getSession().setAttribute(PASSWORD_CHANGE_SUCCESS, true);
            }

            result = new CommandResult(SETTINGS_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'change password' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
