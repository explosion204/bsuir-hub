package com.karnyshov.bsuirhub.controller.command.impl.admin;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.service.GroupService;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.NEW_ENTITY_PAGE;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.PREVIOUS_NAME;

@Named
public class GoToEditGroupPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GroupService groupService;

    @Inject
    private DepartmentService departmentService;

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            Optional<Group> group = groupService.findById(entityId);

            if (group.isPresent()) {
                long departmentId = group.get().getDepartmentId();
                long headmanId = group.get().getHeadmanId();
                long curatorId = group.get().getCuratorId();

                Optional<Department> department = departmentService.findById(departmentId);
                Optional<User> headman = userService.findById(headmanId);
                Optional<User> curator = userService.findById(curatorId);

                department.ifPresent(value -> request.setAttribute(DEPARTMENT_NAME, value.getName()));
                headman.ifPresent(value -> request.setAttribute(HEADMAN_LAST_NAME, value.getLastName()));
                curator.ifPresent(value -> request.setAttribute(CURATOR_LAST_NAME, value.getLastName()));

                request.setAttribute(TARGET_ENTITY, group.get());
                request.getSession().setAttribute(PREVIOUS_NAME, group.get().getName());
                request.setAttribute(NEW_ENTITY_PAGE, false);
                result = new CommandResult(ADMIN_VIEW_GROUP_JSP, FORWARD);
            } else {
                result = new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);
        } catch (ServiceException | NoSuchElementException e) {
            logger.error("An error occurred executing 'go to edit group page' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
