package com.karnyshov.bsuirhub.controller.command.impl.admin.user;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.*;

/**
 * {@code GoToEditUserPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GoToEditUserPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            Optional<User> user = userService.findById(entityId);

            if (user.isPresent() && user.get().getStatus() != UserStatus.DELETED) {
                request.setAttribute(TARGET_ENTITY, user.get());
                request.setAttribute(ENTITY_ID, user.get().getEntityId());

                HttpSession session = request.getSession();
                session.setAttribute(PREVIOUS_EMAIL, user.get().getEmail());
                session.setAttribute(PREVIOUS_ROLE, user.get().getRole());

                request.setAttribute(NEW_ENTITY_PAGE, false);
                result = new CommandResult(ADMIN_VIEW_USER_JSP, FORWARD);
            } else {
                result = new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(NOT_FOUND_ERROR_URL, REDIRECT);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to edit user page' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
