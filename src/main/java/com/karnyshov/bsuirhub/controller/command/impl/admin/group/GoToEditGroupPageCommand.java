package com.karnyshov.bsuirhub.controller.command.impl.admin.group;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.NEW_ENTITY_PAGE;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.PREVIOUS_NAME;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code GoToEditGroupPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToEditGroupPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private GroupService groupService;

    @Inject
    public GoToEditGroupPageCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            Optional<Group> group = groupService.findById(entityId);

            if (group.isPresent() && !group.get().isArchived()) {
                request.setAttribute(TARGET_ENTITY, group.get());
                request.getSession().setAttribute(PREVIOUS_NAME, group.get().getName());
                request.setAttribute(NEW_ENTITY_PAGE, false);
                result = new CommandResult(ADMIN_VIEW_GROUP_JSP, FORWARD);
            } else {
                result = new CommandResult(SC_NOT_FOUND, ERROR);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(SC_NOT_FOUND, ERROR);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to edit group page' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
