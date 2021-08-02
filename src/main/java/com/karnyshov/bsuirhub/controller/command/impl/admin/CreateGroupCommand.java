package com.karnyshov.bsuirhub.controller.command.impl.admin;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.validator.GroupValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

@Named
public class CreateGroupCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GroupService groupService;

    @Inject
    private GroupValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        String name = request.getParameter(NAME);

        try {
            long departmentId = Long.parseLong(request.getParameter(DEPARTMENT_ID));
            long curatorId = Long.parseLong(request.getParameter(CURATOR_ID));

            Group group = Group.builder()
                    .setDepartmentId(departmentId)
                    .setCuratorId(curatorId)
                    .setName(name)
                    .build();

            if (validator.validateGroup(request, group)) {
                // data is valid
                groupService.create(group);
                result = new CommandResult(ADMIN_GROUPS_URL, REDIRECT);
            } else {
                // data is not valid
                result = new CommandResult(ADMIN_NEW_GROUP_URL, REDIRECT);
            }
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'create group' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
