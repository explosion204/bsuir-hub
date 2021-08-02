package com.karnyshov.bsuirhub.controller.command.impl.admin;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.DepartmentValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

@Named
public class CreateDepartmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private DepartmentService departmentService;

    @Inject
    private DepartmentValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

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

            if (validator.validateDepartment(request, department)) {
                // data is valid
                departmentService.create(department);
                result = new CommandResult(ADMIN_DEPARTMENTS_URL, REDIRECT);
            } else {
                // data is not valid
                result = new CommandResult(ADMIN_NEW_DEPARTMENT_URL, REDIRECT);
            }
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'create department' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
