package com.karnyshov.bsuirhub.controller.command.impl.admin.group;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ADMIN_GROUPS_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

/**
 * {@code GoToGroupsPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToGroupsPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(ADMIN_GROUPS_JSP, FORWARD);
    }
}
