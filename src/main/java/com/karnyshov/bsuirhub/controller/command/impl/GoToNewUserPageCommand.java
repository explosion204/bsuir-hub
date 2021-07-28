package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ADMIN_VIEW_USER_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.NEW_ENTITY_PAGE;

@Named
public class GoToNewUserPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        request.setAttribute(NEW_ENTITY_PAGE, true);
        return new CommandResult(ADMIN_VIEW_USER_JSP, FORWARD);
    }
}
