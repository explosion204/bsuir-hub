package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.ApplicationPath;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@code GoToIndexPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GoToIndexPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult(ApplicationPath.INDEX_JSP, CommandResult.RouteType.FORWARD);
    }
}
