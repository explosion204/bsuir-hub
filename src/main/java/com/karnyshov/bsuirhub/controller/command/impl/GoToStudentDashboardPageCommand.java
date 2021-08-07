package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.GradeService;
import com.karnyshov.bsuirhub.model.service.GroupService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INTERNAL_SERVER_ERROR_URL;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.STUDENT_DASHBOARD_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.AVERAGE_STUDY_GRADE;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.GROUP_NAME;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

@Named
public class GoToStudentDashboardPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GradeService gradeService;

    @Inject
    private GroupService groupService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        User student = (User) request.getSession().getAttribute(USER);
        long studentId = student.getEntityId();
        long groupId = student.getGroupId();

        try {
            groupService.findById(groupId).ifPresent(group -> request.setAttribute(GROUP_NAME, group.getName()));

            double averageGradeValue = gradeService.calculateAverage(studentId);
            request.setAttribute(AVERAGE_STUDY_GRADE, averageGradeValue);

            result = new CommandResult(STUDENT_DASHBOARD_JSP, FORWARD);
        } catch (ServiceException e) {
            logger.error("An error occurred executing 'go to student dashboard page' command", e);
            result = new CommandResult(INTERNAL_SERVER_ERROR_URL, REDIRECT);
        }

        return result;
    }
}
