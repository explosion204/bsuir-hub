package com.karnyshov.bsuirhub.controller;

import com.karnyshov.bsuirhub.controller.command.*;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.NOT_FOUND_ERROR_URL;
import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;

@WebServlet(
        urlPatterns = { "/controller" }
)
@MultipartConfig(
        fileSizeThreshold = 1024, // 1 kb
        maxFileSize = 1024 * 512, // 512 kb
        maxRequestSize = 1024 * 1024 * 3 // 3 mb
)
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private static final String PAGES_PATH = "/WEB-INF/pages/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        try {
            processRequest(request, response);
        } catch (MultiPartParserDefinition.FileTooLargeException e) {
            logger.error("Trying to upload too large file");
            response.sendRedirect(ApplicationPath.INTERNAL_SERVER_ERROR_URL);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        String url = (String) request.getAttribute(ORIGINAL_URL);
        Optional<Command> command = CommandProvider.getInstance().getCommand(url, method);

        if (command.isPresent()) {
            CommandResult commandResult = command.get().execute(request);

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
                    response.sendRedirect(ApplicationPath.INTERNAL_SERVER_ERROR_URL);
            }
        } else {
            response.sendRedirect(NOT_FOUND_ERROR_URL);
        }
    }
}
