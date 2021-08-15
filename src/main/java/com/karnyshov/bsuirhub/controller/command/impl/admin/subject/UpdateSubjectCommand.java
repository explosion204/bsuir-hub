package com.karnyshov.bsuirhub.controller.command.impl.admin.subject;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.service.SubjectService;
import com.karnyshov.bsuirhub.model.validator.SubjectValidator;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code UpdateSubjectCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class UpdateSubjectCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private SubjectService subjectService;

    @Inject
    public UpdateSubjectCommand(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String idString = request.getParameter(ENTITY_ID);
        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);

        Subject subject = Subject.builder()
                .setName(name)
                .setShortName(shortName)
                .setArchived(false)
                .build();

        boolean validationResult = SubjectValidator.validateSubject(subject);
        session.setAttribute(VALIDATION_ERROR, !validationResult);

        // data is valid
        try {
            if (validationResult) {
                long entityId = Long.parseLong(idString);
                Subject updatedSubject = (Subject) Subject.builder()
                        .of(subject)
                        .setEntityId(entityId)
                        .build();

                subjectService.update(updatedSubject);

                // success
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
            }

            String url = new UrlStringBuilder(ADMIN_EDIT_SUBJECT_URL)
                    .addParam(ENTITY_ID, idString)
                    .build();

            result = new CommandResult(url, REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'update subject' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
