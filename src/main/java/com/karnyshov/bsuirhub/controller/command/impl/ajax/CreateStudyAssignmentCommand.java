package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;
import com.karnyshov.bsuirhub.model.service.StudyAssignmentService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;

@Named
public class CreateStudyAssignmentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private StudyAssignmentService studyAssignmentService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = true;

        try {
            long groupId = Long.parseLong(request.getParameter(GROUP_ID));
            long teacherId = Long.parseLong(request.getParameter(TEACHER_ID));
            long subjectId = Long.parseLong(request.getParameter(SUBJECT_ID));

            StudyAssignment assignment = StudyAssignment.builder()
                    .setGroupId(groupId)
                    .setTeacherId(teacherId)
                    .setSubjectId(subjectId)
                    .build();

            long entityId = studyAssignmentService.create(assignment);
            response.put(ASSIGNMENT_ID, entityId);
        } catch (NumberFormatException | ServiceException e) {
            logger.error("An error occurred executing 'create study assignment' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
