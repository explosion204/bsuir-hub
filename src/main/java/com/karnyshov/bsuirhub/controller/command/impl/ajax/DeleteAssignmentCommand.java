package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.AssignmentService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code DeleteAssignmentCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class DeleteAssignmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private AssignmentService assignmentService;

    @Inject
    public DeleteAssignmentCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = true;

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            assignmentService.delete(entityId);
        } catch (NumberFormatException | ServiceException e) {
            logger.error("An error occurred executing 'delete assignment' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
