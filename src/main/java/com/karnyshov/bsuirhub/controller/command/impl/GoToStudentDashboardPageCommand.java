package com.karnyshov.bsuirhub.controller.command.impl;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.*;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INTERNAL_SERVER_ERROR_URL;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.STUDENT_DASHBOARD_JSP;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.FORWARD;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.REDIRECT;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code GoToStudentDashboardPageCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GoToStudentDashboardPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private GradeService gradeService;
    private GroupService groupService;
    private FacultyService facultyService;
    private DepartmentService departmentService;
    private UserService userService;

    @Inject
    public GoToStudentDashboardPageCommand(GradeService gradeService, GroupService groupService,
                FacultyService facultyService, DepartmentService departmentService, UserService userService) {
        this.gradeService = gradeService;
        this.groupService = groupService;
        this.facultyService = facultyService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandResult result;
        User student = (User) request.getSession().getAttribute(USER);
        long studentId = student.getEntityId();
        long groupId = student.getGroupId();

        try {
            Optional<Group> optionalGroup = groupService.findById(groupId);
            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                long departmentId = group.getDepartmentId();
                long curatorId = group.getCuratorId();
                long headmanId = group.getHeadmanId();

                request.setAttribute(GROUP, group);
                userService.findById(curatorId).ifPresent(curator -> request.setAttribute(CURATOR, curator));
                userService.findById(headmanId).ifPresent(headman -> request.setAttribute(HEADMAN, headman));
                Optional<Department> optionalDepartment = departmentService.findById(departmentId);

                if (optionalDepartment.isPresent()) {
                    Department department = optionalDepartment.get();
                    long facultyId = department.getFacultyId();

                    request.setAttribute(DEPARTMENT, department);
                    facultyService.findById(facultyId).ifPresent(faculty -> request.setAttribute(FACULTY, faculty));
                }
            }

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
