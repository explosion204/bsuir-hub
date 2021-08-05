package com.karnyshov.bsuirhub.controller.command.impl.ajax;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.ENTITY_ID;
import static com.karnyshov.bsuirhub.model.entity.UserRole.*;

@Named
public class GetUsersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        String typeValue = request.getParameter(REQUEST_TYPE);
        Map<String, Object> response = new HashMap<>();
        boolean status = true;
        
        if (typeValue != null) {
            AjaxRequestType issuer = AjaxRequestType.valueOf(typeValue.toUpperCase());
            
            try {
                switch (issuer) {
                    case FETCH_BY_ID:
                        processFetchByIdRequest(request, response);
                        break;
                    case JQUERY_DATATABLE:
                        processDatatableRequest(request, response);
                        break;
                    case JQUERY_SELECT:
                        processSelectRequest(request, response);
                        break;
                    default:
                        status = false;
                }
            } catch (ServiceException | IllegalArgumentException e) {
                logger.error("An error occurred executing 'get users' command", e);
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
        String searchValue = request.getParameter(SEARCH_VALUE);

        List<User> users = new LinkedList<>();

        long recordsFetched = searchCriteria != null
                ? userService.filter(page, length, UserFilterCriteria.valueOf(searchCriteria.toUpperCase()),
                        searchValue, users)
                : userService.filter(page, length, users);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, users);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response) throws ServiceException {
        String searchValue = request.getParameter(TERM);
        int page = Integer.parseInt(request.getParameter(PAGE));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE));
        boolean fetchStudents = Boolean.parseBoolean(request.getParameter(FETCH_STUDENTS));
        String groupIdString = request.getParameter(GROUP_ID);

        List<User> users;
        long recordsFetched;

        if (fetchStudents) {
            // get all students for requested group
            users = new LinkedList<>(); // TODO: 8/4/2021 ArrayList vs LinkedList
            // to determine if pagination is required we use amount of fetched records BEFORE filtering by last name
            recordsFetched = userService.filter(page, pageSize, UserFilterCriteria.GROUP, groupIdString, users);
            // filter by last name
            users = users.stream().filter(u -> StringUtils.containsIgnoreCase(u.getLastName(), searchValue))
                    .collect(Collectors.toList());
        } else {
            List<User> admins = new LinkedList<>();
            List<User> teachers = new LinkedList<>();

            // fetch teachers
            recordsFetched = userService.filter(page, pageSize, UserFilterCriteria.ROLE,
                    String.valueOf(TEACHER.ordinal()), teachers);

            // fetch admins
            recordsFetched += userService.filter(page, pageSize, UserFilterCriteria.ROLE,
                    String.valueOf(ADMIN.ordinal()), admins);

            teachers.addAll(admins);
            users = teachers;
        }

        response.put(RESULTS, users);
        response.put(PAGINATION_MORE, (long) page * pageSize < recordsFetched);
    }

    private void processFetchByIdRequest(HttpServletRequest request, Map<String, Object> response)
            throws ServiceException {
        long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
        Optional<User> user = userService.findById(entityId);
        user.ifPresent(value -> response.put(ENTITY, value));
        response.put(ENTITY, user.orElse(null));
    }
}
