package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.TokenService;
import com.karnyshov.bsuirhub.util.MailService;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.PASSWORD_RESET_LINK_SENT;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.CONFIRMED;

/**
 * {@code SendResetPasswordLinkCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class SendResetPasswordLinkCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String PROTOCOL_DELIMITER = "://";
    private static final String SUBJECT_PROPERTY = "password_reset.subject";
    private static final String BODY_PROPERTY = "password_reset.message";
    private static final String ID_CLAIM = "id";
    private static final String SALT_CLAIM = "salt";

    private UserService userService;
    private TokenService tokenService;
    private MailService mailService;

    @Inject
    public SendResetPasswordLinkCommand(UserService userService, TokenService tokenService, MailService mailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

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
                Map<String, Object> claims = new HashMap<>() {{ put(ID_CLAIM, userId); put(SALT_CLAIM, salt); }};
                String token = tokenService.generateToken(claims);
                String url = new UrlStringBuilder(RESET_PASSWORD_URL)
                        .addParam(TOKEN, token)
                        .build();
                String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName() + ":8080" + url;

                String subject = mailService.getMailProperty(SUBJECT_PROPERTY);
                String bodyTemplate = mailService.getMailProperty(BODY_PROPERTY);
                String mailBody = String.format(bodyTemplate, confirmationLink);
                mailService.sendMail(email, subject, mailBody);
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
