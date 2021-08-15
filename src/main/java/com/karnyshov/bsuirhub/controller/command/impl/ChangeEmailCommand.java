package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.UserValidator;
import com.karnyshov.bsuirhub.util.TokenService;
import com.karnyshov.bsuirhub.util.MailService;
import com.karnyshov.bsuirhub.util.UniqueValuesCache;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.NOT_CONFIRMED;

/**
 * {@code ChangeEmailCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class ChangeEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UniqueValuesCache uniqueValues = UniqueValuesCache.getInstance();
    private static final String PROTOCOL_DELIMITER = "://";

    private static final String SUBJECT_PROPERTY = "confirmation_mail.subject";
    private static final String BODY_PROPERTY = "confirmation_mail.body";

    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";

    private UserService userService;
    private TokenService tokenService;
    private MailService mailService;

    @Inject
    public ChangeEmailCommand(UserService userService, TokenService tokenService, MailService mailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();
        String email = request.getParameter(EMAIL);

        try {
            User user = (User) request.getSession().getAttribute(USER);
            long targetId = user.getEntityId();

            boolean validationResult = UserValidator.validateEmail(email);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            // logic for resending confirmation link
            // if user submitted the same email as his current not confirmed from session, uniqueness check will be omitted
            // and activation link will be resent
            boolean isEmailUnique = StringUtils.equals(user.getEmail(), email) && user.getStatus() == NOT_CONFIRMED
                    || uniqueValues.add(email) && userService.isEmailUnique(email);
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
                Map<String, Object> claims = new HashMap<>() {{ put(ID_CLAIM, targetId); put(EMAIL_CLAIM, email); }};
                String token = tokenService.generateToken(claims);
                String url = new UrlStringBuilder(CONFIRM_EMAIL_URL)
                        .addParam(TOKEN, token)
                        .build();
                String confirmationLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName() + ":8080" + url;

                String subject = mailService.getMailProperty(SUBJECT_PROPERTY);
                String bodyTemplate = mailService.getMailProperty(BODY_PROPERTY);
                String mailBody = String.format(bodyTemplate, confirmationLink);
                mailService.sendMail(email, subject, mailBody);

                // update target user session if exists
                request.getSession().setAttribute(USER, updatedUser);
            }

            result = new CommandResult(SETTINGS_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'change email' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        } finally {
            uniqueValues.remove(email);
        }

        return result;
    }
}