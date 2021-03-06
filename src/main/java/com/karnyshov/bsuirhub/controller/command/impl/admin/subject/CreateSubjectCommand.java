package com.karnyshov.bsuirhub.controller.command.impl.admin.subject;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.service.SubjectService;
import com.karnyshov.bsuirhub.model.validator.SubjectValidator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.ERROR;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.NAME;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.SHORT_NAME;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * {@code CreateSubjectCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class CreateSubjectCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private SubjectService subjectService;

    @Inject
    public CreateSubjectCommand(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);

        Subject subject = Subject.builder()
                .setName(name)
                .setShortName(shortName)
                .setArchived(false)
                .build();

        boolean validationResult = SubjectValidator.validateSubject(subject);
        session.setAttribute(VALIDATION_ERROR, !validationResult);

        if (validationResult) {
            // data is valid
            try {
                subjectService.create(subject);
                result = new CommandResult(ADMIN_SUBJECTS_URL, REDIRECT);
            } catch (ServiceException e) {
                logger.error("An error occurred executing 'create subject' command", e);
                result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
            }
        } else {
            // data is not valid
            result = new CommandResult(ADMIN_NEW_SUBJECT_URL, REDIRECT);
        }

        return result;
    }
}
