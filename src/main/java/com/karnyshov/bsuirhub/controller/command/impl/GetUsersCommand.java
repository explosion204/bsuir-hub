package com.karnyshov.bsuirhub.controller.command.impl;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.service.criteria.UserFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PAGINATION_LENGTH;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.PAGINATION_START;

@Named
public class GetUsersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

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
                        // TODO: 7/22/2021
                        break;
                }
                
                response.put(STATUS, true);
            } catch (ServiceException | NumberFormatException | EnumConstantNotPresentException e) {
                logger.error("An error occurred executing 'get users' command", e);
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

        List<User> users = new LinkedList<>();

        long recordsTotal = searchCriteria != null
                ? userService.filter(page, length, UserFilterCriteria.valueOf(searchCriteria), searchValue, users)
                : userService.filter(page, length, users);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsTotal);
        response.put(RECORDS_FILTERED, users.size());
        response.put(DATA, users);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response) {

    }
}
