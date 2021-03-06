package com.karnyshov.bsuirhub.controller.command.impl.admin.user;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.listener.HttpSessionAttributeListenerImpl;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.ERROR;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code DeleteUserCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class DeleteUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService;

    @Inject
    public DeleteUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        String idString = request.getParameter(ENTITY_ID);
        User issuer = (User) request.getSession().getAttribute(USER);

        try {
            long entityId = Long.parseLong(idString);
            Optional<User> targetUser = userService.findById(entityId);

            // prevent user from deleting himself or another administrator
            if (issuer.getEntityId() != entityId || (targetUser.isPresent()
                    && targetUser.get().getRole() != UserRole.ADMIN)) {

                userService.delete(entityId);
                // force deleted user logout
                HttpSessionAttributeListenerImpl.findSession(entityId).ifPresent(
                        targetSession -> targetSession.removeAttribute(USER));

                result = new CommandResult(ADMIN_USERS_URL, REDIRECT);
            } else {
                result = new CommandResult(SC_NOT_FOUND, ERROR);
            }
        } catch (NumberFormatException e) {
            result = new CommandResult(SC_NOT_FOUND, ERROR);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'delete user' command", e);
            result = new CommandResult(SC_INTERNAL_SERVER_ERROR, ERROR);
        }

        return result;
    }
}
