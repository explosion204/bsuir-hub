package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.validator.CommentValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.CommentService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

@Named
public class CreateCommentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private static final String AMPERSAND = "&";
    private static final String LESS_THEN = "<";
    private static final String GREATER_THEN = ">";
    private static final String QUOTES = "\"";
    private static final String APOSTROPHE = "'";
    private static final String ESCAPED_AMPERSAND = "&amp;";
    private static final String ESCAPED_LESS_THEN = "&lt;";
    private static final String ESCAPED_GREATER_THEN = "&gt;";
    private static final String ESCAPED_QUOTES = "&quot;";
    private static final String ESCAPED_APOSTROPHE = "&#x27;";

    @Inject
    private CommentService commentService;

    @Inject
    private CommentValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status;
        User currentUser = (User) request.getSession().getAttribute(USER);

        try {
            long gradeId = Long.parseLong(request.getParameter(GRADE_ID));
            long userId = currentUser.getEntityId();
            String text = escapeString(request.getParameter(COMMENT_TEXT));

            Comment comment = Comment.builder()
                    .setGradeId(gradeId)
                    .setUserId(userId)
                    .setText(text)
                    .setCreationTime(LocalDateTime.now())
                    .build();

            status = validator.validateComment(comment);

            if (status) {
                long entityId = commentService.create(comment);
                response.put(COMMENT_ID, entityId);
            }
        } catch (NumberFormatException | ServiceException e) {
            logger.error("An error occurred executing 'create comment' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }

    private String escapeString(String target) {
        return target.replace(AMPERSAND, ESCAPED_AMPERSAND)
                .replace(GREATER_THEN, ESCAPED_GREATER_THEN)
                .replace(LESS_THEN, ESCAPED_LESS_THEN)
                .replace(QUOTES, ESCAPED_QUOTES)
                .replace(APOSTROPHE, ESCAPED_APOSTROPHE);
    }
}
