package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.TokenService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.TOKEN;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER_ID;

@Named
public class GoToResetPasswordPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            boolean success = false;
            String token = request.getParameter(TOKEN);
            Optional<Pair<Long, String>> tokenContent = tokenService.parsePasswordResetToken(token);

            if (tokenContent.isPresent()) {
                Pair<Long, String> pair = tokenContent.get();
                long userId = pair.getLeft();
                String salt = pair.getRight();

                Optional<User> optionalUser = userService.findById(userId);

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    // store target user id in session to use it later to change password
                    request.getSession().setAttribute(USER_ID, userId);
                    success = StringUtils.equals(salt, user.getSalt());
                }
            }

            result = success
                    ? new CommandResult(RESET_PASSWORD_JSP, FORWARD)
                    : new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);

        } catch (ServiceException e) {
            logger.error("An error occurred executing 'confirm email' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
