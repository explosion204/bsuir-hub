package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;

@Named
public class GetDepartmentsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private static final String FACULTY_ID_PROPERTY = "facultyId";
    private static final String FACULTY_NAME_PROPERTY = "facultyName";
    private static final String EMPTY_VALUE = "";

    @Inject
    private DepartmentService departmentService;

    @Inject
    private FacultyService facultyService;

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
                        // TODO: 7/31/2021
                        break;
                    default:
                        status = false;
                }
            } catch (ServiceException | NumberFormatException | EnumConstantNotPresentException e) {
                logger.error("An error occurred executing 'get departments' command", e);
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

        List<Department> departments = new LinkedList<>();

        long recordsFetched = searchCriteria != null
                ? departmentService.filter(page, length, DepartmentFilterCriteria.valueOf(searchCriteria), searchValue, departments)
                : departmentService.filter(page, length, departments);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);

        JsonArray objects = gson.toJsonTree(departments).getAsJsonArray();
        for (JsonElement object : objects) {
            JsonObject jsonObject = object.getAsJsonObject();
            long facultyId = jsonObject.get(FACULTY_ID_PROPERTY).getAsLong();
            Optional<Faculty> faculty = facultyService.findById(facultyId);
            jsonObject.addProperty(FACULTY_NAME_PROPERTY, faculty.isPresent() ? faculty.get().getName() : EMPTY_VALUE);
        }

        response.put(DATA, objects);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response) {

    }
}
