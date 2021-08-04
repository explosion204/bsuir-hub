package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.TokenService;
import com.karnyshov.bsuirhub.util.MailService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.PASSWORD_RESET_LINK_SENT;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.EMAIL;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.LOCALE;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.CONFIRMED;

@Named
public class SendResetPasswordLinkCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String BUNDLE_NAME = "locale";
    private static final String PROTOCOL_DELIMITER = "://";
    private static final String SUBJECT_PROPERTY = "password_reset.subject";
    private static final String MESSAGE_PROPERTY = "password_reset.message";

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @Inject
    private MailService mailService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            String email = request.getParameter(EMAIL);
            Optional<User> user = userService.findByEmail(email);

            if (user.isPresent() && user.get().getStatus() == CONFIRMED) {
                long userId = user.get().getEntityId();
                String salt = user.get().getSalt();

                // send confirmation mail
                // FIXME: production link
                // String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName()
                //        + CONFIRM_EMAIL_URL + jwtService.generateEmailConfirmationToken(targetId);

                // FIXME: development link
                String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName() + ":8080"
                        + RESET_PASSWORD_URL + tokenService.generatePasswordResetToken(userId, salt);

                HttpSession session = request.getSession();
                String locale = (String) session.getAttribute(LOCALE);

                if (locale != null) {
                    ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(locale));
                    String subject = bundle.getString(SUBJECT_PROPERTY);
                    String message = bundle.getString(MESSAGE_PROPERTY);
                    String mailBody = message + confirmationLink;
                    mailService.sendMail(email, subject, mailBody);
                }
            }

            request.getSession().setAttribute(PASSWORD_RESET_LINK_SENT, true);
            result = new CommandResult(FORGOT_PASSWORD_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'send password reset link' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
