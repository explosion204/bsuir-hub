package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.ApplicationPath;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.LOGIN_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

public class GoToLoginPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, List<String> commandParams) {
        return new CommandResult(LOGIN_JSP, FORWARD);
    }
}
