package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ERROR_INTERNAL_SERVER_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

public class GoToInternalErrorPage implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, List<String> commandParams) {
        return new CommandResult(ERROR_INTERNAL_SERVER_JSP, FORWARD);
    }
}
