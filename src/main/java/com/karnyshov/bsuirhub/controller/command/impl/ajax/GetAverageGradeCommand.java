package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.service.GradeService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code GetAverageGradeCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GetAverageGradeCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private GradeService gradeService;

    @Inject
    public GetAverageGradeCommand(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = true;

        try {
            long studentId = Long.parseLong(request.getParameter(STUDENT_ID));
            long subjectId = Long.parseLong(request.getParameter(SUBJECT_ID));

            double averageGradeValue = gradeService.calculateAverageBySubject(studentId, subjectId);
            response.put(AVG_VALUE, averageGradeValue);
        } catch (ServiceException | NumberFormatException e) {
            logger.error("An error occurred executing 'get average grade' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
