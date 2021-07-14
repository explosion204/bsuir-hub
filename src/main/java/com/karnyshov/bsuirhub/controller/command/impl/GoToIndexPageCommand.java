package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.PagePath;
import jakarta.servlet.http.HttpServletRequest;

public class GoToIndexPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, String... commandParams) {
        return new CommandResult(PagePath.INDEX_PAGE, CommandResult.RouteType.FORWARD);
    }
}
