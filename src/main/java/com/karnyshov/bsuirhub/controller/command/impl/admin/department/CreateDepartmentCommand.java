package com.karnyshov.bsuirhub.controller.command.impl.admin.department;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.validator.DepartmentValidator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.ERROR;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * {@code CreateDepartmentCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class CreateDepartmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private DepartmentService departmentService;

    @Inject
    public CreateDepartmentCommand(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String name = request.getParameter(NAME);
        String shortName = request.getParameter(SHORT_NAME);
        String specialtyAlias = request.getParameter(SPECIALTY_ALIAS);

        try {
            long facultyId = Long.parseLong(request.getParameter(FACULTY_ID));
            Department department = Department.builder()
                    .setName(name)
                    .setShortName(shortName)
                    .setSpecialtyAlias(specialtyAlias)
                    .setFacultyId(facultyId)
                    .setArchived(false)
                    .build();

            boolean validationResult = DepartmentValidator.validateDepartment(department);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            if (validationResult) {
                // data is valid
                departmentService.create(department);
                result = new CommandResult(ADMIN_DEPARTMENTS_URL, REDIRECT);
            } else {
                // data is not valid
                result = new CommandResult(ADMIN_NEW_DEPARTMENT_URL, REDIRECT);
            }
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'create department' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
