package com.karnyshov.bsuirhub.controller.command.impl.admin.group;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import com.karnyshov.bsuirhub.model.validator.GroupValidator;
import com.karnyshov.bsuirhub.util.UniqueValuesCache;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.NOT_UNIQUE_NAME;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.VALIDATION_ERROR;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code CreateGroupCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class CreateGroupCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UniqueValuesCache uniqueValues = UniqueValuesCache.getInstance();
    private GroupService groupService;

    @Inject
    public CreateGroupCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();
        String name = request.getParameter(NAME);

        try {
            long departmentId = Long.parseLong(request.getParameter(DEPARTMENT_ID));
            long curatorId = Long.parseLong(request.getParameter(CURATOR_ID));

            Group group = Group.builder()
                    .setDepartmentId(departmentId)
                    .setCuratorId(curatorId)
                    .setName(name)
                    .build();

            boolean validationResult = GroupValidator.validateGroup(group);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            boolean isNameUnique = uniqueValues.add(name) && groupService.isNameUnique(name);
            if (!isNameUnique) {
                session.setAttribute(NOT_UNIQUE_NAME, true);
            }

            if (validationResult && isNameUnique) {
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
        } finally {
            uniqueValues.remove(name);
        }

        return result;
    }
}
