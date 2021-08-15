package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.AssignmentService;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.LOGIN_URL;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.NOT_FOUND_ERROR_URL;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.STUDENT;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code GradesOverviewAccessFilter} class is an implementation of {@link Filter} interface.
 * This filter controls access of users to URLs like "/grades/*".
 * @author Dmitry Karnyshov
 */
@WebFilter(filterName = "GradesOverviewAccessFilter")
public class GradesOverviewAccessFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @Inject
    private AssignmentService assignmentService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        boolean canAccess = false;
        UserRole role = UserRole.GUEST;

        try {
            long subjectId = Long.parseLong(request.getParameter(SUBJECT_ID));
            long studentId = Long.parseLong(request.getParameter(STUDENT_ID));

            User currentUser = (User) session.getAttribute(USER);
            role = currentUser.getRole();

            // user whose overview we want to access
            User student = null;

            if (role == UserRole.STUDENT && currentUser.getEntityId() == studentId) {
                // let user in if he is a STUDENT, and he is trying to access HIS overview page
                student = currentUser;
                long groupId = currentUser.getGroupId();
                canAccess = assignmentService.assignmentExists(groupId, subjectId);
            } else if (role == UserRole.TEACHER || role == UserRole.ADMIN) {
                // if user is TEACHER or ADMIN, we need to check if he has appropriate assignment
                Optional<User> optionalStudent = userService.findById(studentId);

                if (optionalStudent.isPresent()) {
                    student = optionalStudent.get();
                    long groupId = student.getGroupId();
                    long teacherId = currentUser.getEntityId();
                    canAccess = assignmentService.assignmentExists(groupId, teacherId, subjectId);
                }
            }

            // store retrieved user as request attribute to optimize access to database
            request.setAttribute(STUDENT, student);
        } catch (NumberFormatException | ServiceException e) {
            logger.error("An error occurred trying to access grade overview page", e);
        }

        if (canAccess) {
            chain.doFilter(request, response);
        } else {
            // guest will be redirected to login page or to not found page otherwise
            String redirectUrl;

            if (role == UserRole.GUEST) {
                String returnUrl = new UrlStringBuilder(httpRequest.getRequestURI())
                        .build(httpRequest.getQueryString());
                session.setAttribute(RETURN_URL, returnUrl);
                redirectUrl = LOGIN_URL;
            } else {
                redirectUrl = NOT_FOUND_ERROR_URL;
            }

            httpResponse.sendRedirect(redirectUrl);
        }
    }
}
