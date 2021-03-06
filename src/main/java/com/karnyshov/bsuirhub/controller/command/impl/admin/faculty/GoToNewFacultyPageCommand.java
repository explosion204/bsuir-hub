package com.karnyshov.bsuirhub.controller.command.impl.admin.faculty;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ADMIN_VIEW_FACULTY_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.NEW_ENTITY_PAGE;

/**
 * {@code GoToNewFacultyPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToNewFacultyPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        request.setAttribute(NEW_ENTITY_PAGE, true);
        return new CommandResult(ADMIN_VIEW_FACULTY_JSP, FORWARD);
    }
}
