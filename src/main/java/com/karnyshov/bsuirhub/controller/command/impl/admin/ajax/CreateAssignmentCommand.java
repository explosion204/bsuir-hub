package com.karnyshov.bsuirhub.controller.command.impl.admin.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Assignment;
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
 * {@code CreateAssignmentCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class CreateAssignmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private AssignmentService assignmentService;

    @Inject
    public CreateAssignmentCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = true;

        try {
            long groupId = Long.parseLong(request.getParameter(GROUP_ID));
            long teacherId = Long.parseLong(request.getParameter(TEACHER_ID));
            long subjectId = Long.parseLong(request.getParameter(SUBJECT_ID));

            Assignment assignment = Assignment.builder()
                    .setGroupId(groupId)
                    .setTeacherId(teacherId)
                    .setSubjectId(subjectId)
                    .build();

            long entityId = assignmentService.create(assignment);
            response.put(ASSIGNMENT_ID, entityId);
        } catch (NumberFormatException | ServiceException e) {
            logger.error("An error occurred executing 'create assignment' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
