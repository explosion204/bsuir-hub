package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.RequestParameter;
import com.karnyshov.bsuirhub.controller.command.SessionAttribute;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;

@Named
public class SetLocaleCommand implements Command {
    private static final String JSON_RESULT = "";

    @Override
    public CommandResult execute(HttpServletRequest request) {
        String locale = request.getParameter(RequestParameter.LOCALE);
        request.getSession().setAttribute(SessionAttribute.LOCALE, locale);

        return new CommandResult(JSON_RESULT, JSON);
    }
}
