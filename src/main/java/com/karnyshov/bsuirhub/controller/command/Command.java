package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.Controller;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@code Command} interface implementors are mapped to many URLs and performs various actions.
 * This architectural approach lets to move all business logic from {@link Controller} class to separate classes.
 * @author Dmitry Karnyshov
 */
public interface Command {
    /**
     * Execute command.
     *
     * @param request instance of {@link HttpServletRequest} from controller.
     * @return {@link CommandResult} instance.
     */
    CommandResult execute(HttpServletRequest request);
}
