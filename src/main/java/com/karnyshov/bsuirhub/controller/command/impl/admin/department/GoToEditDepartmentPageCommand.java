package com.karnyshov.bsuirhub.controller.command.impl.admin.department;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code GoToEditDepartmentPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToEditDepartmentPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private DepartmentService departmentService;

    @Inject
    public GoToEditDepartmentPageCommand(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            Optional<Department> department = departmentService.findById(entityId);

            if (department.isPresent() && !department.get().isArchived()) {
                request.setAttribute(TARGET_ENTITY, department.get());
                request.setAttribute(NEW_ENTITY_PAGE, false);
                result = new CommandResult(ADMIN_VIEW_DEPARTMENT_JSP, FORWARD);
            } else {
                result = new CommandResult(SC_NOT_FOUND, ERROR);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(SC_NOT_FOUND, ERROR);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to edit department page' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
