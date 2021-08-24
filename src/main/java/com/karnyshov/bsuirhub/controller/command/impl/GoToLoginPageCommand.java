package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.LOGIN_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.RETURN_URL;

/**
 * {@code GoToLoginPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToLoginPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        request.setAttribute(RETURN_URL, session.getAttribute(RETURN_URL));
        return new CommandResult(LOGIN_JSP, FORWARD);
    }
}
