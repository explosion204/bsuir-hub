package com.karnyshov.bsuirhub.controller;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.CommandType;
import com.karnyshov.bsuirhub.controller.command.RequestMethod;
import com.karnyshov.bsuirhub.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;

@WebServlet(
        urlPatterns = { "/main" }
)
public class MainController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private static final String QUERY_PARAMS_DELIMITER = "/";
    private static final String EMPTY_ACTION = "";
    private static final String PAGES_PATH = "/WEB-INF/pages/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        String url = (String) request.getAttribute(ORIGINAL_URL);
        String[] queryParams = url.split(QUERY_PARAMS_DELIMITER);
        String action = queryParams.length != 0
                ? queryParams[1]
                : EMPTY_ACTION;
        String[] urlParams = queryParams.length != 0
                ? Arrays.copyOfRange(queryParams, 2, queryParams.length)
                : new String[0];

        try {
            Command command = CommandType.getCommand(action, method);
            CommandResult commandResult = command.execute(request, urlParams);

            String routePath = commandResult.getRoutePath();
            CommandResult.RouteType routeType = commandResult.getRouteType();

            switch (routeType) {
                case FORWARD:
                    request.getRequestDispatcher(PAGES_PATH + routePath).forward(request, response);
                    break;
                case REDIRECT:
                    response.sendRedirect(routePath);
                    break;
                default:
                    logger.error("Invalid route type: " + routeType.name());
                    // TODO: 7/12/2021 error pages & logs
            }
        } catch (CommandException e) {
            throw new RuntimeException(e); // TODO: 7/14/2021 remove RuntimeException and forward to error page & logs
        }
    }

    // TODO: 7/15/2021 destroy pool in context listener
}
