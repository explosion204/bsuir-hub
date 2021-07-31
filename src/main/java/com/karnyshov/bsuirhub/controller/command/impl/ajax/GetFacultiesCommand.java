package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import com.karnyshov.bsuirhub.model.service.criteria.FacultyFilterCriteria;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PAGINATION_LENGTH;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PAGINATION_START;

public class GetFacultiesCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private FacultyService facultyService;

    private enum RequestType {
        JQUERY_DATATABLE, JQUERY_SELECT
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        String typeValue = request.getParameter(REQUEST_TYPE);
        Map<String, Object> response = new HashMap<>();

        if (typeValue != null) {
            RequestType issuer = RequestType.valueOf(typeValue.toUpperCase());

            try {
                switch (issuer) {
                    case JQUERY_DATATABLE:
                        processDatatableRequest(request, response);
                        break;
                    case JQUERY_SELECT:
                        processSelectRequest(request, response);
                        // TODO: 7/31/2021
                        break;
                }

                response.put(STATUS, true);
            } catch (ServiceException | NumberFormatException | EnumConstantNotPresentException e) {
                logger.error("An error occurred executing 'get faculties' command", e);
                response.put(STATUS, false);
            }

        } else {
            response.put(STATUS, false);
        }

        return new CommandResult(new Gson().toJson(response), JSON);
    }

    private void processDatatableRequest(HttpServletRequest request, Map<String, Object> response)
            throws ServiceException, NumberFormatException {

        int start = Integer.parseInt(request.getParameter(PAGINATION_START));
        int length = Integer.parseInt(request.getParameter(PAGINATION_LENGTH));
        int page = start / length + 1;

        int draw = Integer.parseInt(request.getParameter(DRAW));
        String searchCriteria = request.getParameter(FILTER_CRITERIA);
        String searchValue = request.getParameter(SEARCH_VALUE);

        List<Faculty> faculties = new LinkedList<>();

        long recordsFetched = searchCriteria != null
                ? facultyService.filter(page, length, FacultyFilterCriteria.valueOf(searchCriteria), searchValue, faculties)
                : facultyService.filter(page, length, faculties);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, faculties);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response) {

    }
}
