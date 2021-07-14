package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.PagePath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.*;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String AUTH_ERROR_MESSAGE = "Invalid login or password";

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request, String ... commandParams) {
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        CommandResult result;

        try {
            Optional<User> user = userService.authenticate(login, password);

            if (user.isPresent()) {
                request.getSession().setAttribute(USER, user.get());
                result = new CommandResult(INDEX_PAGE, REDIRECT);
            } else {
                request.setAttribute(LOGIN_ERROR, AUTH_ERROR_MESSAGE);
                result = new CommandResult(LOGIN_PAGE, FORWARD);
            }
        } catch (ServiceException e) {
            logger.error("An error occurred executing login command", e);
            result = new CommandResult(ERROR_PAGE, REDIRECT);
        }

        return result;
    }
}
