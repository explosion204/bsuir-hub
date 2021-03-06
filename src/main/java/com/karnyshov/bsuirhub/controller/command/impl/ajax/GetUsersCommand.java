package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.service.criteria.UserFilterCriteria;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;
import static com.karnyshov.bsuirhub.model.entity.UserRole.*;

/**
 * {@code GetUsersCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GetUsersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private UserService userService;

    @Inject
    public GetUsersCommand(UserService userService) {
        this.userService = userService;
    }

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
            throws ServiceException {
        int start = Integer.parseInt(request.getParameter(PAGINATION_START));
        int length = Integer.parseInt(request.getParameter(PAGINATION_LENGTH));

        int draw = Integer.parseInt(request.getParameter(DRAW));
        String searchCriteria = request.getParameter(FILTER_CRITERIA);
        String searchValue = request.getParameter(SEARCH_VALUE);

        List<User> users = new ArrayList<>(length);

        int recordsFetched = searchCriteria != null
                ? userService.filter(start, length, UserFilterCriteria.valueOf(searchCriteria.toUpperCase()),
                        searchValue, users)
                : userService.filter(start, length, users);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, users);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response)
            throws ServiceException {
        String searchValue = request.getParameter(TERM);
        int page = Integer.parseInt(request.getParameter(PAGE));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE));
        int start = pageSize * (page - 1);

        boolean fetchStudents = Boolean.parseBoolean(request.getParameter(FETCH_STUDENTS));
        String groupIdString = request.getParameter(GROUP_ID);

        List<User> users;
        int recordsFetched;

        if (fetchStudents) {
            // get all students for requested group
            users = new ArrayList<>(pageSize);
            // to determine if pagination is required we use amount of fetched records BEFORE filtering by last name
            recordsFetched = userService.filter(start, pageSize, UserFilterCriteria.GROUP, groupIdString, users);
            // filter by last name
            users = users.stream().filter(u -> StringUtils.containsIgnoreCase(u.getLastName(), searchValue))
                    .collect(Collectors.toList());
        } else {
            List<User> admins = new ArrayList<>(pageSize);
            List<User> teachers = new ArrayList<>(pageSize);

            // fetch teachers
            recordsFetched = userService.filter(start, pageSize, UserFilterCriteria.ROLE,
                    String.valueOf(TEACHER.ordinal()), teachers);

            // fetch admins
            recordsFetched += userService.filter(start, pageSize, UserFilterCriteria.ROLE,
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
