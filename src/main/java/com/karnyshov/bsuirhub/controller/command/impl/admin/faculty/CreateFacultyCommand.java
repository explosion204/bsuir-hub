package com.karnyshov.bsuirhub.controller.command.impl.admin.faculty;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import com.karnyshov.bsuirhub.model.validator.NewFacultyValidator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

@Named
public class CreateFacultyCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private FacultyService facultyService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);

        Faculty faculty = Faculty.builder()
                .setName(name)
                .setShortName(shortName)
                .setArchived(false)
                .build();

        boolean validationResult = NewFacultyValidator.validateFaculty(faculty);
        session.setAttribute(VALIDATION_ERROR, !validationResult);

        if (validationResult) {
            // data is valid
            try {
                facultyService.create(faculty);
                result = new CommandResult(ADMIN_FACULTIES_URL, REDIRECT);
            } catch (ServiceException e) {
                logger.error("An error occurred executing 'create faculty' command", e);
                result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
            }
        } else {
            // data is not valid
            result = new CommandResult(ADMIN_NEW_FACULTY_URL, REDIRECT);
        }

        return result;
    }
}
