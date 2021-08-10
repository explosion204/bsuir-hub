package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.validator.NewUserValidator;
import com.karnyshov.bsuirhub.model.validator.PlainTextValidator;
import com.karnyshov.bsuirhub.util.MailService;
import com.karnyshov.bsuirhub.util.impl.MailServiceImpl;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

@Named
public class SendMessageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String SUBJECT = "BSUIRHUB | Contact request";
    private static final String BODY_CONTACT_EMAIL = "<b>Contact email:</b> ";
    private static final String BODY_TEXT = "<br/><b>Message:</b> ";

    private static final String MAIL_PROPERTIES_NAME = "mail.properties";
    private static final String SUPPORT_PROPERTY = "support";
    private static final String supportMail;

    @Inject
    private MailService mailService;

    static {
        ClassLoader classLoader = MailServiceImpl.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(MAIL_PROPERTIES_NAME)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            supportMail = properties.getProperty(SUPPORT_PROPERTY);
        } catch (IOException e) {
            logger.fatal("Unable to read mailing properties", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();
        String contactEmail = request.getParameter(EMAIL);
        String text = request.getParameter(TEXT);

        boolean validationResult = NewUserValidator.validateEmail(contactEmail) && PlainTextValidator.validateText(text);
        session.setAttribute(VALIDATION_ERROR, !validationResult);

        try {
            if (validationResult) {
                String body = BODY_CONTACT_EMAIL + contactEmail + BODY_TEXT + text;
                mailService.sendMail(supportMail, SUBJECT, body);
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
