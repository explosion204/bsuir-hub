package com.karnyshov.bsuirhub.controller.command.impl.admin.department;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.validator.DepartmentValidator;
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
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.FACULTY_ID;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * {@code UpdateDepartmentCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class UpdateDepartmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private DepartmentService departmentService;

    @Inject
    public UpdateDepartmentCommand(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

        String idString = request.getParameter(ENTITY_ID);
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
                long entityId = Long.parseLong(idString);
                Department updatedDepartment = (Department) Department.builder()
                        .of(department)
                        .setEntityId(entityId)
                        .build();

                departmentService.update(updatedDepartment);
                session.setAttribute(ENTITY_UPDATE_SUCCESS, true);
            }

            String url = new UrlStringBuilder(ADMIN_EDIT_DEPARTMENT_URL)
                    .addParam(ENTITY_ID, idString)
                    .build();

            result = new CommandResult(url, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'update department' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
