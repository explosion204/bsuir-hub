package com.karnyshov.bsuirhub.controller.command.impl.admin.group;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import com.karnyshov.bsuirhub.model.validator.GroupValidator;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.PREVIOUS_NAME;

@Named
public class UpdateGroupCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GroupService groupService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        HttpSession session = request.getSession();

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

            boolean validationResult = GroupValidator.validateGroup(group);
            session.setAttribute(VALIDATION_ERROR, !validationResult);

            boolean nameNotChanged = StringUtils.equals(name, previousName);
            boolean isNameUnique = nameNotChanged || groupService.isNameUnique(name);
            if (!isNameUnique) {
                session.setAttribute(NOT_UNIQUE_NAME, true);
            }

            if (validationResult && isNameUnique) {
                // data is valid
                long entityId = Long.parseLong(idString);
                Group updatedGroup = (Group) Group.builder()
                        .of(group)
                        .setEntityId(entityId)
                        .build();

                groupService.update(updatedGroup);
                request.getSession().setAttribute(ENTITY_UPDATE_SUCCESS, true);
            }

            String url = new UrlStringBuilder(ADMIN_EDIT_GROUP_URL)
                    .addParam(ENTITY_ID, idString)
                    .build();

            result = new CommandResult(url, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'update group' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
