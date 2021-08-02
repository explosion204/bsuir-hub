package com.karnyshov.bsuirhub.controller.command.impl.admin.subject;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.SubjectValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.service.SubjectService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

@Named
public class UpdateSubjectCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private SubjectService subjectService;

    @Inject
    private SubjectValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        String idString = request.getParameter(ENTITY_ID);
        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);

        Subject subject = Subject.builder()
                .setName(name)
                .setShortName(shortName)
                .setArchived(false)
                .build();

        if (validator.validateSubject(request, subject)) {
            // data is valid
            try {
                long entityId = Long.parseLong(idString);
                Subject updatedSubject = (Subject) Subject.builder()
                        .of(subject)
                        .setEntityId(entityId)
                        .build();

                subjectService.update(updatedSubject);

                // success
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
                result = new CommandResult(ADMIN_EDIT_SUBJECT_URL + idString, REDIRECT);
            } catch (ServiceException | NumberFormatException e) {
                logger.error("An error occurred executing 'update subject' command", e);
                result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
            }
        } else {
            // data is not valid
            result = new CommandResult(ADMIN_EDIT_SUBJECT_URL + idString, REDIRECT);
        }

        return result;
    }
}
