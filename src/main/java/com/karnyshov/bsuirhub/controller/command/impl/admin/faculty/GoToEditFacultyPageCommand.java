package com.karnyshov.bsuirhub.controller.command.impl.admin.faculty;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.NEW_ENTITY_PAGE;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.TARGET_ENTITY;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code GoToEditFacultyPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToEditFacultyPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private FacultyService facultyService;

    @Inject
    public GoToEditFacultyPageCommand(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            Optional<Faculty> faculty = facultyService.findById(entityId);

            if (faculty.isPresent() && !faculty.get().isArchived()) {
                request.setAttribute(TARGET_ENTITY, faculty.get());
                request.setAttribute(ENTITY_ID, faculty.get().getEntityId());
                request.setAttribute(NEW_ENTITY_PAGE, false);
                result = new CommandResult(ADMIN_VIEW_FACULTY_JSP, FORWARD);
            } else {
                result = new CommandResult(SC_NOT_FOUND, ERROR);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(SC_NOT_FOUND, ERROR);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to edit faculty page' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
