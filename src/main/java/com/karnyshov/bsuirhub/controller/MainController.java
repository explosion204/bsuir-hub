package com.karnyshov.bsuirhub.controller;

import com.karnyshov.bsuirhub.controller.command.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.NOT_FOUND_ERROR_URL;
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

        List<String> commandParams = new ArrayList<>();
        Optional<Command> command = CommandType.parseCommand(url, method, commandParams);

        if (command.isPresent()) {
            CommandResult commandResult = command.get().execute(request, commandParams);

            String resultDetail = commandResult.getDetail();
            CommandResult.RouteType routeType = commandResult.getRouteType();

            switch (routeType) {
                case FORWARD:
                    request.getRequestDispatcher(PAGES_PATH + resultDetail).forward(request, response);
                    break;
                case REDIRECT:
                    response.sendRedirect(resultDetail);
                    break;
                case JSON:
                    response.getWriter().write(resultDetail);
                    break;
                default:
                    logger.error("Invalid route type: " + routeType.name());
                    // TODO: 7/12/2021 error pages & logs
            }
        } else {
            response.sendRedirect(NOT_FOUND_ERROR_URL);
        }
    }

    // TODO: 7/15/2021 destroy pool in context listener
}
