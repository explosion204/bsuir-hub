package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.TokenService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.EMAIL_CONFIRMATION_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.TOKEN;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.CONFIRMED;

/**
 * {@code ConfirmEmailCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class ConfirmEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";

    private UserService userService;
    private TokenService tokenService;

    @Inject
    public ConfirmEmailCommand(UserService userService, TokenService tokenService) {
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
                String email = (String) tokenContent.get(EMAIL_CLAIM);

                Optional<User> optionalUser = userService.findById(userId);

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    if (StringUtils.equals(email, user.getEmail()) && user.getStatus() != CONFIRMED) {
                        User updatedUser = User.builder().of(user)
                                .setStatus(CONFIRMED)
                                .build();
                        userService.update(updatedUser);
                        success = true;

                        HttpSession session = request.getSession();
                        session.setAttribute(EMAIL_CONFIRMATION_SUCCESS, true);

                        // update user session if exists
                        session.setAttribute(USER, updatedUser);
                    }
                }
            }

            result = success
                    ? new CommandResult(INDEX_URL, REDIRECT)
                    : new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);

        } catch (ServiceException e) {
            logger.error("An error occurred executing 'confirm email' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
