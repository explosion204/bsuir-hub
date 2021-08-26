package com.karnyshov.bsuirhub.controller.command.impl.admin.group;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.GroupService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.ERROR;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code DeleteGroupCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class DeleteGroupCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private GroupService groupService;

    @Inject
    public DeleteGroupCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        String idString = request.getParameter(ENTITY_ID);

        try {
            long entityId = Long.parseLong(idString);
            groupService.delete(entityId);
            result = new CommandResult(ADMIN_GROUPS_URL, REDIRECT);
        } catch (NumberFormatException e) {
            result = new CommandResult(SC_NOT_FOUND, ERROR);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'delete group' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
