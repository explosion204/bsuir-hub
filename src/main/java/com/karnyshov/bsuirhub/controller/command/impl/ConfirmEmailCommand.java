package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.JwtService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.EMAIL_CONFIRMATION_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.CONFIRMATION_TOKEN;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.CONFIRMED;

@Named
public class ConfirmEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Inject
    private JwtService jwtService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            boolean success = false;
            String token = request.getParameter(CONFIRMATION_TOKEN);
            Optional<Pair<Long, String>> jwtContent = jwtService.parseJwt(token);

            if (jwtContent.isPresent()) {
                Pair<Long, String> pair = jwtContent.get();
                long userId = pair.getLeft();
                String email = pair.getRight();

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

            // TODO: 7/28/2021 invalidate token (maybe with timer task?)
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
