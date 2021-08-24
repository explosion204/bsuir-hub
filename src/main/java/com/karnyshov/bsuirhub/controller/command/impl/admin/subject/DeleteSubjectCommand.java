package com.karnyshov.bsuirhub.controller.command.impl.admin.subject;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.SubjectService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;

/**
 * {@code DeleteSubjectCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class DeleteSubjectCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private SubjectService subjectService;

    @Inject
    public DeleteSubjectCommand(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        String idString = request.getParameter(ENTITY_ID);

        try {
            long entityId = Long.parseLong(idString);
            subjectService.delete(entityId);
            result = new CommandResult(ADMIN_SUBJECTS_URL, REDIRECT);
        } catch (NumberFormatException e) {
            result = new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'delete subject' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
