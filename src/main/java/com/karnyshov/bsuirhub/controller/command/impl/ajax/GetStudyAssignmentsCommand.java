package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;
import com.karnyshov.bsuirhub.model.service.StudyAssignmentService;
import com.karnyshov.bsuirhub.model.service.criteria.StudyAssignmentFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.impl.ajax.AjaxRequestType.JQUERY_DATATABLE;

@Named
public class GetStudyAssignmentsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();

    @Inject
    private StudyAssignmentService assignmentService;

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
                logger.error("An error occurred executing 'get study assignments' command", e);
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
        int page = start / length + 1;

        int draw = Integer.parseInt(request.getParameter(DRAW));
        String searchCriteria = request.getParameter(FILTER_CRITERIA);
        long searchId = Long.parseLong(request.getParameter(SEARCH_VALUE));

        List<StudyAssignment> assignments = new LinkedList<>();

        long recordsFetched = 0;
        if (searchCriteria != null) {
            recordsFetched = assignmentService.filter(page, length, StudyAssignmentFilterCriteria.valueOf(searchCriteria.toUpperCase()),
                    searchId, assignments);
        }

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, assignments);
    }
}