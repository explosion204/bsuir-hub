package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.NewUserValidator;
import com.karnyshov.bsuirhub.util.TokenService;
import com.karnyshov.bsuirhub.util.MailService;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.NOT_CONFIRMED;

@Named
public class ChangeEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String BUNDLE_NAME = "locale";
    private static final String PROTOCOL_DELIMITER = "://";
    private static final String SUBJECT_PROPERTY = "confirmation_mail.subject";
    private static final String MESSAGE_PROPERTY = "confirmation_mail.message";

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @Inject
    private MailService mailService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        try {
            User user = (User) request.getSession().getAttribute(USER);
            long targetId = user.getEntityId();

            String email = request.getParameter(EMAIL);
            boolean validationResult = NewUserValidator.validateEmail(email);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            // logic for resending confirmation link
            // if user submitted the same email as his current not confirmed from session, uniqueness check will be omitted
            // and activation link will be resent
            boolean isEmailUnique = StringUtils.equals(user.getEmail(), email) && user.getStatus() == NOT_CONFIRMED
                    || userService.isEmailUnique(email);
            if (!isEmailUnique) {
                session.setAttribute(NOT_UNIQUE_EMAIL, true);
            }

            if (validationResult && isEmailUnique) {
                // data is valid
                User updatedUser = userService.changeEmail(targetId, email);
                session.setAttribute(EMAIL_CHANGE_SUCCESS, true);

                // send confirmation mail
                // FIXME: production link
                // String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName()
                //        + CONFIRM_EMAIL_URL + jwtService.generateJwt(targetId);

                // FIXME: development link
                String token = tokenService.generateEmailConfirmationToken(targetId, email);
                String url = new UrlStringBuilder(CONFIRM_EMAIL_URL)
                        .addParam(TOKEN, token)
                        .build();
                String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName() + ":8080" + url;

                String locale = (String) session.getAttribute(LOCALE);
                ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(locale));

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