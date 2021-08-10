package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.GradeService;
import com.karnyshov.bsuirhub.model.validator.GradeValidator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserRole.ADMIN;
import static com.karnyshov.bsuirhub.model.entity.UserRole.TEACHER;

@Named
public class CreateGradeCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GradeService gradeService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status;

        User currentUser = (User) request.getSession().getAttribute(USER);
        UserRole role = currentUser.getRole();

        // filter grants access to area /grades/* BUT we have to prevent malicious attempts to POST data by students
        if (role == TEACHER || role == ADMIN) {
            try {
                long studentId = Long.parseLong(request.getParameter(STUDENT_ID));
                long teacherId = currentUser.getEntityId();
                long subjectId = Long.parseLong(request.getParameter(SUBJECT_ID));
                byte gradeValue = Byte.parseByte(request.getParameter(GRADE_VALUE));

                status = GradeValidator.validateGradeValue(gradeValue);

                if (status) {
                    Grade grade = Grade.builder()
                            .setStudentId(studentId)
                            .setTeacherId(teacherId)
                            .setSubjectId(subjectId)
                            .setValue(gradeValue)
                            .setDate(LocalDate.now())
                            .build();

                    gradeService.create(grade);
                }
            } catch (ServiceException | IllegalArgumentException e) {
                logger.error("An error occurred executing 'create grade' command", e);
                status = false;
            }
        } else {
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
