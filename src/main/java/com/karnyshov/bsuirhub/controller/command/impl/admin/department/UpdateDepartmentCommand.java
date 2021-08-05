package com.karnyshov.bsuirhub.controller.command.impl.admin.department;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.DepartmentValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.FACULTY_ID;

@Named
public class UpdateDepartmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private DepartmentService departmentService;

    @Inject
    private DepartmentValidator departmentValidator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

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

            if (departmentValidator.validateDepartment(request, department)) {
                // data is valid
                long entityId = Long.parseLong(idString);
                Department updatedDepartment = (Department) Department.builder()
                        .of(department)
                        .setEntityId(entityId)
                        .build();

                departmentService.update(updatedDepartment);
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
            }

            String url = new UrlStringBuilder(ADMIN_EDIT_DEPARTMENT_URL)
                    .addParam(ENTITY_ID, idString)
                    .build();

            result = new CommandResult(url, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'update department' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
