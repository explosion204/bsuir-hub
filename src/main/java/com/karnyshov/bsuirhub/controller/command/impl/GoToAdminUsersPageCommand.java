package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ADMIN_USERS_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

public class GoToAdminUsersPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, List<String> commandParams) {
        return new CommandResult(ADMIN_USERS_JSP, FORWARD);
    }
}
