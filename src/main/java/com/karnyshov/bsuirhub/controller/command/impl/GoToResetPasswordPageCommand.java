package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.TokenService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.TOKEN;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER_ID;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code GoToResetPasswordPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToResetPasswordPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String ID_CLAIM = "id";
    private static final String SALT_CLAIM = "salt";
    private UserService userService;
    private TokenService tokenService;

    @Inject
    public GoToResetPasswordPageCommand(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            boolean success = false;
            String token = request.getParameter(TOKEN);
            Map<String, Object> tokenContent = tokenService.parseToken(token);

            if (!tokenContent.isEmpty()) {
                long userId = ((Double) tokenContent.get(ID_CLAIM)).longValue();
                String salt = (String) tokenContent.get(SALT_CLAIM);

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
                    : new CommandResult(SC_NOT_FOUND, ERROR);

        } catch (ServiceException e) {
            logger.error("An error occurred executing 'confirm email' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
