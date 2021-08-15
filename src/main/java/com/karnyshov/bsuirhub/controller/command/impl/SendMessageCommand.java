package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.validator.UserValidator;
import com.karnyshov.bsuirhub.model.validator.PlainTextValidator;
import com.karnyshov.bsuirhub.util.MailService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code SendMessageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class SendMessageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String SUPPORT_PROPERTY = "support";
    private static final String SUBJECT_PROPERTY = "contact_request.subject";
    private static final String BODY_PROPERTY = "contact_request.body";
    private MailService mailService;

    @Inject
    public SendMessageCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();
        String contactEmail = request.getParameter(EMAIL);
        String text = request.getParameter(TEXT);

        boolean validationResult = UserValidator.validateEmail(contactEmail) && PlainTextValidator.validateText(text);
        session.setAttribute(VALIDATION_ERROR, !validationResult);

        try {
            if (validationResult) {
                String supportMail = mailService.getMailProperty(SUPPORT_PROPERTY);
                String subject = mailService.getMailProperty(SUBJECT_PROPERTY);
                String bodyTemplate = mailService.getMailProperty(BODY_PROPERTY);
                String mailBody = String.format(bodyTemplate, contactEmail, text);
                mailService.sendMail(supportMail, subject, mailBody);
                session.setAttribute(MESSAGE_SENT, true);
            }

            result = new CommandResult(CONTACT_US_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'send message' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
