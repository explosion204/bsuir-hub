package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.GradeService;
import com.karnyshov.bsuirhub.model.service.SubjectService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.GRADES_OVERVIEW_JSP;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INTERNAL_SERVER_ERROR_URL;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.GRADE_ID;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.SUBJECT_ID;

/**
 * {@code GoToGradesOverviewPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GoToGradesOverviewPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private SubjectService subjectService;

    @Inject
    private GradeService gradeService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        long subjectId = Long.parseLong(request.getParameter(SUBJECT_ID)); // value is trusted
        User student = (User) request.getAttribute(STUDENT);

        try {
            subjectService.findById(subjectId)
                    .ifPresent(subject -> request.setAttribute(SUBJECT, subject));

            String gradeIdParameter = request.getParameter(GRADE_ID);
            if (NumberUtils.isParsable(gradeIdParameter)) {
                long gradeId = Long.parseLong(gradeIdParameter);
                request.setAttribute(GRADE_ID, gradeId);
            }

            long studentId = student.getEntityId();
            double averageGrade = gradeService.calculateAverageBySubject(studentId, subjectId);
            request.setAttribute(AVERAGE_STUDY_GRADE, averageGrade);

            // set average subject grade
            result = new CommandResult(GRADES_OVERVIEW_JSP, FORWARD);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to grades overview page' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
