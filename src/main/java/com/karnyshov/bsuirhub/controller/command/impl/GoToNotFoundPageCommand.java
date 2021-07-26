package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;


import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ERROR_NOT_FOUND_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

public class GoToNotFoundPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(ERROR_NOT_FOUND_JSP, FORWARD);
    }
}
