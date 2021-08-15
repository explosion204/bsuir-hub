package com.karnyshov.bsuirhub.controller.command.impl.admin.faculty;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;

/**
 * {@code DeleteFacultyCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class DeleteFacultyCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private FacultyService facultyService;

    @Inject
    public DeleteFacultyCommand(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        String idString = request.getParameter(ENTITY_ID);

        try {
            long entityId = Long.parseLong(idString);
            facultyService.delete(entityId);
            result = new CommandResult(ADMIN_FACULTIES_URL, REDIRECT);
        } catch (NumberFormatException e) {
            result = new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'delete faculty' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
