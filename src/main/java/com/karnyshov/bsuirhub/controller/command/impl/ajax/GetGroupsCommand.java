package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code GetGroupsCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class GetGroupsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();

    @Inject
    private GroupService groupService;

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
                logger.error("An error occurred executing 'get groups' command", e);
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
        String searchCriteria = request.getParameter(FILTER_CRITERIA);
        String searchValue = request.getParameter(SEARCH_VALUE);

        List<Group> groups = new LinkedList<>();

        long recordsFetched = searchCriteria != null
                ? groupService.filter(start, length, GroupFilterCriteria.valueOf(searchCriteria.toUpperCase()),
                        searchValue, groups)
                : groupService.filter(start, length, groups);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, groups);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response) throws ServiceException {
        String searchValue = request.getParameter(TERM);
        int page = Integer.parseInt(request.getParameter(PAGE));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE));
        int start = pageSize * (page - 1);

        List<Group> groups = new LinkedList<>();
        long recordsFetched = groupService.filter(start, pageSize, GroupFilterCriteria.NAME, searchValue, groups);
        response.put(RESULTS, groups);
        response.put(RECORDS_FILTERED, recordsFetched);
    }

    private void processFetchByIdRequest(HttpServletRequest request, Map<String, Object> response)
            throws ServiceException {
        long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
        Optional<Group> group = groupService.findById(entityId);
        group.ifPresent(value -> response.put(ENTITY, value));
        response.put(ENTITY, group.orElse(null));
    }
}
