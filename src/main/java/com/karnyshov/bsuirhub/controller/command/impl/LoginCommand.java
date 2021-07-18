package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.SessionAttribute;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.LOGIN_ERROR;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.LOGIN;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PASSWORD;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;


public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String AUTH_ERROR_MESSAGE = "Invalid login or password";

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request, String ... urlParams) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute(SessionAttribute.USER);

        if (sessionUser.getUserRole() != UserRole.GUEST) {
            return new CommandResult(INDEX_URL, REDIRECT);
        }

        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        CommandResult result;

        try {
            Optional<User> user = userService.authenticate(login, password);

            if (user.isPresent()) {
                session.setAttribute(USER, user.get());
                result = new CommandResult(INDEX_URL, REDIRECT);
            } else {
                request.setAttribute(LOGIN_ERROR, AUTH_ERROR_MESSAGE);
                result = new CommandResult(LOGIN_JSP, FORWARD);
            }
        } catch (ServiceException e) {
            logger.error("An error occurred executing login command", e);
            result = new CommandResult(ERROR_JSP, REDIRECT);
        }

        return result;
    }
}
