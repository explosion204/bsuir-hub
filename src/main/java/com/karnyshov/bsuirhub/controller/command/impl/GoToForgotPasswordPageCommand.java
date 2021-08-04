package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.FORGOT_PASSWORD_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

public class GoToForgotPasswordPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(FORGOT_PASSWORD_JSP, FORWARD);
    }
}
