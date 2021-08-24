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

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.*;

/**
 * {@code GetFacultiesCommand} class is an implementation of {@link Command} interface.
 * @author Dmitry Karnyshov
 */
public class GetFacultiesCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private FacultyService facultyService;

    @Inject
    public GetFacultiesCommand(FacultyService facultyService) {
        this.facultyService = facultyService;
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
                logger.error("An error occurred executing 'get faculties' command", e);
                status = false;
            }
        } else {
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }

    private void processDatatableRequest(HttpServletRequest request, Map<String, Object> response)
            throws ServiceException {
        int start = Integer.parseInt(request.getParameter(PAGINATION_START));
        int length = Integer.parseInt(request.getParameter(PAGINATION_LENGTH));

        int draw = Integer.parseInt(request.getParameter(DRAW));
        String searchCriteria = request.getParameter(FILTER_CRITERIA);
        String searchValue = request.getParameter(SEARCH_VALUE);

        List<Faculty> faculties = new LinkedList<>();

        int recordsFetched = searchCriteria != null
                ? facultyService.filter(start, length, FacultyFilterCriteria.valueOf(searchCriteria.toUpperCase()),
                        searchValue, faculties)
                : facultyService.filter(start, length, faculties);

        response.put(DRAW, draw);
        response.put(RECORDS_TOTAL, recordsFetched);
        response.put(RECORDS_FILTERED, recordsFetched);
        response.put(DATA, faculties);
    }

    private void processSelectRequest(HttpServletRequest request, Map<String, Object> response)
                throws ServiceException {
        String searchValue = request.getParameter(TERM);
        int page = Integer.parseInt(request.getParameter(PAGE));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE));
        int start = pageSize * (page - 1);

        List<Faculty> faculties = new LinkedList<>();
        int recordsFetched = facultyService.filter(start, pageSize, FacultyFilterCriteria.NAME,
                searchValue, faculties);
        response.put(RESULTS, faculties);
        response.put(PAGINATION_MORE, (long) page * pageSize < recordsFetched);
    }

    private void processFetchByIdRequest(HttpServletRequest request, Map<String, Object> response)
                throws ServiceException {
        long entityId = Long.parseLong(request.getParameter(ENTITY_ID));
        Optional<Faculty> faculty = facultyService.findById(entityId);
        faculty.ifPresent(value -> response.put(ENTITY, value));
        response.put(ENTITY, faculty.orElse(null));
    }
}
