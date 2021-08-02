package com.karnyshov.bsuirhub.controller.command.impl.admin;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ADMIN_FACULTIES_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

@Named
public class GoToFacultiesPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(ADMIN_FACULTIES_JSP, FORWARD);
    }
}
