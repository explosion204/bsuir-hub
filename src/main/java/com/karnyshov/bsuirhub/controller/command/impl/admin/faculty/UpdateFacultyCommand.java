package com.karnyshov.bsuirhub.controller.command.impl.admin.faculty;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import com.karnyshov.bsuirhub.model.validator.FacultyValidator;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.ERROR;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * {@code UpdateFacultyCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class UpdateFacultyCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private FacultyService facultyService;

    @Inject
    public UpdateFacultyCommand(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String idString = request.getParameter(ENTITY_ID);
        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);

        Faculty faculty = Faculty.builder()
                .setName(name)
                .setShortName(shortName)
                .setArchived(false)
                .build();

        boolean validationResult = FacultyValidator.validateFaculty(faculty);
        session.setAttribute(VALIDATION_ERROR, !validationResult);

        try {
            if (validationResult) {
                // data is valid
                long entityId = Long.parseLong(idString);
                Faculty updatedFaculty = (Faculty) Faculty.builder()
                        .of(faculty)
                        .setEntityId(entityId)
                        .build();

                facultyService.update(updatedFaculty);
                // success
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
            }

            String url = new UrlStringBuilder(ADMIN_EDIT_FACULTY_URL)
                    .addParam(ENTITY_ID, idString)
                    .build();
            result = new CommandResult(url, REDIRECT);
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'update faculty' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
