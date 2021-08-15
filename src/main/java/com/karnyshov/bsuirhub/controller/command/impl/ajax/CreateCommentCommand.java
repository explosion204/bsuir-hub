package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.*;
import com.karnyshov.bsuirhub.model.service.CommentService;
import com.karnyshov.bsuirhub.model.service.GradeService;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.util.MailService;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.GRADES_OVERVIEW_URL;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code CreateCommentCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class CreateCommentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String PROTOCOL_DELIMITER = "://";
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

    private static final String SUBJECT_PROPERTY = "comment_notification.subject";
    private static final String BODY_PROPERTY = "comment_notification.body";

    @Inject
    private CommentService commentService;

    @Inject
    private GradeService gradeService;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = true;
        User currentUser = (User) request.getSession().getAttribute(USER);

        try {
            long gradeId = Long.parseLong(request.getParameter(GRADE_ID));
            long userId = currentUser.getEntityId();
            UserRole role = currentUser.getRole();
            String text = escapeString(request.getParameter(COMMENT_TEXT));

            Comment comment = Comment.builder()
                    .setGradeId(gradeId)
                    .setUserId(userId)
                    .setText(text)
                    .setCreationTime(LocalDateTime.now())
                    .build();
            Optional<Grade> optionalGrade = gradeService.findById(gradeId);;

            // allow student comment all his grades
            // teachers can comment only their grades
            if (role != UserRole.STUDENT && optionalGrade.isPresent()) {
                long teacherId = optionalGrade.get().getTeacherId();
                status = userId == teacherId;
            }

            if (status && optionalGrade.isPresent()) {
                long entityId = commentService.create(comment);
                response.put(COMMENT_ID, entityId);

                // FIXME: development link
                String url = new UrlStringBuilder(GRADES_OVERVIEW_URL)
                        .addParam(SUBJECT_ID, request.getParameter(SUBJECT_ID))
                        .addParam(STUDENT_ID, request.getParameter(STUDENT_ID))
                        .addParam(GRADE_ID, gradeId)
                        .build();
                String commentsLink = request.getScheme() + PROTOCOL_DELIMITER + request.getServerName() + ":8080" + url;

                String subject = mailService.getMailProperty(SUBJECT_PROPERTY);
                String bodyTemplate = mailService.getMailProperty(BODY_PROPERTY);
                String mailBody = String.format(bodyTemplate, commentsLink);

                long teacherId = optionalGrade.get().getTeacherId();
                long studentId = optionalGrade.get().getStudentId();
                // comment creator will not receive notification
                long recipientId = userId == teacherId ? studentId : teacherId;
                Optional<User> recipient = userService.findById(recipientId);

                // notification will be sent only to confirmed email
                if (recipient.isPresent() && recipient.get().getStatus() == UserStatus.CONFIRMED) {
                    String recipientMail = recipient.get().getEmail();
                    mailService.sendMail(recipientMail, subject, mailBody);
                }
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
