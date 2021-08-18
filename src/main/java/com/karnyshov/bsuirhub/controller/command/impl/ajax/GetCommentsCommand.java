package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.service.CommentService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.impl.ajax.AjaxRequestType.JQUERY_DATATABLE;

/**
 * {@code GetCommentsCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GetCommentsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private CommentService commentsService;

    @Inject
    public GetCommentsCommand(CommentService commentsService) {
        this.commentsService = commentsService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        String typeValue = request.getParameter(REQUEST_TYPE);
        Map<String, Object> response = new HashMap<>();
        boolean status = true;

        if (typeValue != null) {
            AjaxRequestType issuer = AjaxRequestType.valueOf(typeValue.toUpperCase());

            try {
                if (issuer == JQUERY_DATATABLE) {
                    processDatatableRequest(request, response);
                } else {
                    status = false;
                }
            } catch (ServiceException | IllegalArgumentException e) {
                logger.error("An error occurred executing 'get comments' command", e);
                status = false;
            }
        } else {
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(gson.toJson(response), JSON);
    }

    private void processDatatableRequest(HttpServletRequest request, Map<String, Object> response)
            throws ServiceException, NumberFormatException {
        int start = Integer.parseInt(request.getParameter(PAGINATION_START));
        int length = Integer.parseInt(request.getParameter(PAGINATION_LENGTH));

        int draw = Integer.parseInt(request.getParameter(DRAW));
        long gradeId = Long.parseLong(request.getParameter(GRADE_ID));

        List<Comment> grades = new LinkedList<>();
        int recordsFetched = commentsService.findByGrade(start, length, gradeId, grades);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, grades);
    }
}
