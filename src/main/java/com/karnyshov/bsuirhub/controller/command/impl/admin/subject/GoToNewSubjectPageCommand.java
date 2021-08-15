package com.karnyshov.bsuirhub.controller.command.impl.admin.subject;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.ADMIN_VIEW_SUBJECT_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.NEW_ENTITY_PAGE;

/**
 * {@code GoToNewSubjectPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GoToNewSubjectPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        request.setAttribute(NEW_ENTITY_PAGE, true);
        return new CommandResult(ADMIN_VIEW_SUBJECT_JSP, FORWARD);
    }
}
