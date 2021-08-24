package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.CommentService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.STATUS;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code DeleteCommentCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class DeleteCommentCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private CommentService commentService;

    @Inject
    public DeleteCommentCommand(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status;
        User currentUser = (User) request.getSession().getAttribute(USER);

        try {
            long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
            Optional<Comment> comment = commentService.findById(entityId);
            status = comment.isPresent() && comment.get().getUserId() == currentUser.getEntityId();

            if (status) {
                commentService.delete(entityId);
            }
        } catch (NumberFormatException | ServiceException e) {
            logger.error("An error occurred executing 'delete comment' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
