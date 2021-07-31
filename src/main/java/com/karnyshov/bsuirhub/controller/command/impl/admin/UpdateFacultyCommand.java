package com.karnyshov.bsuirhub.controller.command.impl.admin;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.validator.DataValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

public class UpdateFacultyCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private FacultyService facultyService;

    @Inject
    private DataValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        String idString = request.getParameter(ENTITY_ID);
        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);

        Faculty faculty = Faculty.builder()
                .setName(name)
                .setShortName(shortName)
                .setArchived(false)
                .build();

        if (validator.validateFaculty(request, faculty)) {
            // data is valid
            try {
                long entityId = Long.parseLong(idString);
                Faculty updatedFaculty = (Faculty) Faculty.builder()
                        .of(faculty)
                        .setEntityId(entityId)
                        .build();

                facultyService.update(updatedFaculty);

                // success
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
                result = new CommandResult(ADMIN_EDIT_FACULTY_URL + idString, REDIRECT);
            } catch (ServiceException | NumberFormatException e) {
                logger.error("An error occurred executing 'update faculty' command", e);
                result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
            }
        } else {
            // data is not valid
            result = new CommandResult(ADMIN_EDIT_USER_URL + idString, REDIRECT);
        }

        return result;
    }
}
