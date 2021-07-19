package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INDEX_URL;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

public class LogoutCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, List<String> commandParams) {
        request.getSession().removeAttribute(USER);
        return new CommandResult(INDEX_URL, REDIRECT);
    }
}
