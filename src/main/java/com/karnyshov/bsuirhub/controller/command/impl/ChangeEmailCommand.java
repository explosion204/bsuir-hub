package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.validator.UserValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.JwtService;
import com.karnyshov.bsuirhub.util.MailService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.EMAIL_CHANGE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

@Named
public class ChangeEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String PROTOCOL_DELIMITER = "://";
    private static final String SUBJECT_PROPERTY = "mail.subject";
    private static final String MESSAGE_PROPERTY = "mail.message";

    @Inject
    private UserService userService;

    @Inject
    private JwtService jwtService;

    @Inject
    private MailService mailService;

    @Inject
    private UserValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            User user = (User) request.getSession().getAttribute(USER);
            long targetId = user.getEntityId();

            String email = request.getParameter(EMAIL);

            if (validator.validateEmail(request, email)) {
                // data is valid
                User updatedUser = userService.changeEmail(targetId, email);
                request.getSession().setAttribute(EMAIL_CHANGE_SUCCESS, true);

                // send confirmation mail
                // FIXME: production link
                // String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName()
                //        + CONFIRM_EMAIL_URL + jwtService.generateJwt(targetId);

                // FIXME: development link
                String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName() + ":8080"
                        + CONFIRM_EMAIL_URL + jwtService.generateJwt(targetId, email);

                HttpSession session = request.getSession();
                String locale = (String) session.getAttribute(LOCALE);
                ResourceBundle bundle = ResourceBundle.getBundle("locale", new Locale(locale));

                String subject = bundle.getString(SUBJECT_PROPERTY);
                String message = bundle.getString(MESSAGE_PROPERTY);
                String mailBody = message + confirmationLink;
                mailService.sendMail(email, subject, mailBody); // TODO: 7/28/2021

                // update target user session if exists
                request.getSession().setAttribute(USER, updatedUser);
            }

            result = new CommandResult(SETTINGS_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'change email' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}