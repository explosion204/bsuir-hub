package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.ApplicationPath;
import jakarta.servlet.http.HttpServletRequest;

public class GoToIndexPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, String... urlParams) {
        return new CommandResult(ApplicationPath.INDEX_JSP, CommandResult.RouteType.FORWARD);
    }
}
