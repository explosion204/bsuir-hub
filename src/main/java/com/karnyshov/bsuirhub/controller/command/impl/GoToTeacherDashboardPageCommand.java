package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.TEACHER_DASHBOARD_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;

/**
 * {@code GoToTeacherDashboardPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToTeacherDashboardPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(TEACHER_DASHBOARD_JSP, FORWARD);
    }
}
