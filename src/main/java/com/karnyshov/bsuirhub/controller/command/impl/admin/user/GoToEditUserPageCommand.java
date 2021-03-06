package com.karnyshov.bsuirhub.controller.command.impl.admin.user;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.*;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.*;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code GoToEditUserPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GoToEditUserPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService;

    @Inject
    public GoToEditUserPageCommand(UserService userService) {
        this.userService = userService;
    }

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
                result = new CommandResult(SC_NOT_FOUND, ERROR);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(SC_NOT_FOUND, ERROR);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to edit user page' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
