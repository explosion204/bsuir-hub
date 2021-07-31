package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.SessionAttribute;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.LOGIN;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PASSWORD;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.AUTH_ERROR;

@Named
public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute(SessionAttribute.USER);

        if (sessionUser.getRole() != UserRole.GUEST) {
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
                session.setAttribute(AUTH_ERROR, true);
                result = new CommandResult(LOGIN_URL, REDIRECT);
            }
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'login' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
