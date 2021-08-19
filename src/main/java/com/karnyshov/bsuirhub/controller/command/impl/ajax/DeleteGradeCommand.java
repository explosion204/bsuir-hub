package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.CommentService;
import com.karnyshov.bsuirhub.model.service.GradeService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.STATUS;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserRole.ADMIN;
import static com.karnyshov.bsuirhub.model.entity.UserRole.TEACHER;

/**
 * {@code DeleteGradeCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class DeleteGradeCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private GradeService gradeService;

    @Inject
    public DeleteGradeCommand(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = false;

        User currentUser = (User) request.getSession().getAttribute(USER);
        UserRole role = currentUser.getRole();

        try {
            // filter grants access to area /grades/* BUT we have to prevent malicious attempts to POST data by students
            if (role == ADMIN || role == TEACHER) {
                long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
                Optional<Grade> optionalGrade = gradeService.findById(entityId);

                // teacher can delete only his own grades
                status = optionalGrade.isPresent() && optionalGrade.get().getTeacherId() == currentUser.getEntityId();

                if (status) {
                    gradeService.delete(entityId);
                }
            }
        } catch (ServiceException | IllegalArgumentException e) {
            logger.error("An error occurred executing 'delete grade' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
