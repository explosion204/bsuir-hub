package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;

import jakarta.servlet.http.HttpServletRequest;


import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.LOGIN_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

public class GoToLoginPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(LOGIN_JSP, FORWARD);
    }
}
