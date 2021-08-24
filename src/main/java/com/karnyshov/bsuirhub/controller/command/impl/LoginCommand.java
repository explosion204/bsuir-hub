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

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.AUTH_ERROR;

/**
 * {@code LoginCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService;

    @Inject
    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute(SessionAttribute.USER);

        if (sessionUser.getRole() != UserRole.GUEST) {
            return new CommandResult(INDEX_URL, REDIRECT);
        }

        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        String returnUrl = request.getParameter(RETURN_URL);
        session.removeAttribute(RETURN_URL);
        CommandResult result;

        try {
            Optional<User> user = userService.authenticate(login, password);

            if (user.isPresent()) {
                session.setAttribute(USER, user.get());
                result = new CommandResult(returnUrl, REDIRECT);
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
