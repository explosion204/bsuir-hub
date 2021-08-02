package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.service.GroupService;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria;
import com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.RECORDS_FILTERED;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;

@Named
public class GetGroupsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private static final String DEPARTMENT_ID_PROPERTY = "departmentId";
    private static final String DEPARTMENT_NAME_PROPERTY = "departmentName";
    private static final String CURATOR_ID_PROPERTY = "curatorId";
    private static final String CURATOR_LAST_NAME_PROPERTY = "curatorLastName";
    private static final String HEADMAN_ID_PROPERTY = "headmanId";
    private static final String HEADMAN_LAST_NAME_PROPERTY = "headmanLastName";
    private static final String EMPTY_VALUE = "";

    @Inject
    private GroupService groupService;

    @Inject
    private DepartmentService departmentService;

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
                    case JQUERY_DATATABLE:
                        processDatatableRequest(request, response);
                        break;
                    case JQUERY_SELECT:
                        processSelectRequest(request, response);
                        break;
                    default:
                        status = false;
                }
            } catch (ServiceException | NumberFormatException | EnumConstantNotPresentException e) {
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
        int page = start / length + 1;

        int draw = Integer.parseInt(request.getParameter(DRAW));
        String searchCriteria = request.getParameter(FILTER_CRITERIA);
        String searchValue = request.getParameter(SEARCH_VALUE);

        List<Group> groups = new LinkedList<>();

        long recordsFetched = searchCriteria != null
                ? groupService.filter(page, length, GroupFilterCriteria.valueOf(searchCriteria), searchValue, groups)
                : groupService.filter(page, length, groups);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);

        JsonArray objects = gson.toJsonTree(groups).getAsJsonArray();
        for (JsonElement object : objects) {
            JsonObject jsonObject = object.getAsJsonObject();
            long departmentId = jsonObject.get(DEPARTMENT_ID_PROPERTY).getAsLong();
            long curatorId = jsonObject.get(CURATOR_ID_PROPERTY).getAsLong();
            long headmanId = jsonObject.get(HEADMAN_ID_PROPERTY).getAsLong();

            Optional<Department> department = departmentService.findById(departmentId);
            Optional<User> curator = userService.findById(curatorId);
            Optional<User> headman = userService.findById(headmanId);

            jsonObject.addProperty(DEPARTMENT_NAME_PROPERTY, department.isPresent()
                    ? department.get().getName()
                    : EMPTY_VALUE);
            jsonObject.addProperty(CURATOR_LAST_NAME_PROPERTY, curator.isPresent()
                    ? curator.get().getLastName()
                    : EMPTY_VALUE);
            jsonObject.addProperty(HEADMAN_LAST_NAME_PROPERTY, headman.isPresent()
                    ? headman.get().getLastName()
                    : EMPTY_VALUE);
        }

        response.put(DATA, objects);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response) throws ServiceException {
        String searchValue = request.getParameter(TERM);
        int page = Integer.parseInt(request.getParameter(PAGE));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE));

        List<Department> departments = new LinkedList<>();
        long recordsFetched = departmentService.filter(page, pageSize, DepartmentFilterCriteria.NAME,
                searchValue, departments);
        response.put(RESULTS, departments);
        response.put(RECORDS_FILTERED, recordsFetched);
    }
}
