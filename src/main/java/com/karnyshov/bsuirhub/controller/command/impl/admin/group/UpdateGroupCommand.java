package com.karnyshov.bsuirhub.controller.command.impl.admin.group;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.GroupValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.ENTITY_UPDATE_SUCCESS;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.PREVIOUS_NAME;

@Named
public class UpdateGroupCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GroupService groupService;

    @Inject
    private GroupValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        String idString = request.getParameter(ENTITY_ID);
        String name = request.getParameter(NAME);
        String previousName = (String) request.getSession().getAttribute(PREVIOUS_NAME);

        try {
            long departmentId = Long.parseLong(request.getParameter(DEPARTMENT_ID));
            long curatorId = Long.parseLong(request.getParameter(CURATOR_ID));
            String headmanIdString = request.getParameter(HEADMAN_ID);
            long headmanId = StringUtils.isNotBlank(headmanIdString) ? Long.parseLong(headmanIdString) : 0;

            Group group = Group.builder()
                    .setName(name)
                    .setDepartmentId(departmentId)
                    .setCuratorId(curatorId)
                    .setHeadmanId(headmanId)
                    .build();

            boolean nameNotChanged = StringUtils.equals(name, previousName);

            if (validator.validateGroup(request, group, nameNotChanged)) {
                // data is valid
                long entityId = Long.parseLong(idString);
                Group updatedGroup = (Group) Group.builder()
                        .of(group)
                        .setEntityId(entityId)
                        .build();

                groupService.update(updatedGroup);
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
            }

            result = new CommandResult(ADMIN_EDIT_GROUP_URL + idString, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'update group' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
