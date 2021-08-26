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

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code Controller} class is a subclass of {@link HttpServlet} class.
 * It processes all requests after filtering.
 * @author Dmitry Karnyshov
 */
@WebServlet(
        urlPatterns = { "/controller" }, loadOnStartup = 0
)
@MultipartConfig(
        fileSizeThreshold = 1024, // 1 kb
        maxFileSize = 1024 * 512, // 512 kb
        maxRequestSize = 1024 * 1024 * 3 // 3 mb
)
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

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
            response.sendError(SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        String url = (String) request.getAttribute(ORIGINAL_URL);
        Optional<Command> command = CommandProvider.getInstance().getCommand(url, method);

        if (command.isPresent()) {
            CommandResult commandResult = command.get().execute(request);

            Object resultDetail = commandResult.getDetail();
            CommandResult.RouteType routeType = commandResult.getRouteType();

            switch (routeType) {
                case FORWARD:
                    String jspPath = (String) resultDetail;
                    request.getRequestDispatcher(jspPath).forward(request, response);
                    break;
                case REDIRECT:
                    String redirectUrl = (String) resultDetail;
                    response.sendRedirect(redirectUrl);
                    break;
                case JSON:
                    String jsonResponse = (String) resultDetail;
                    response.getWriter().write(jsonResponse);
                    break;
                case ERROR:
                    int errorCode = (Integer) resultDetail;
                    response.sendError(errorCode);
                    break;
                default:
                    logger.error("Invalid route type: " + routeType.name());
                    response.sendError(SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(SC_NOT_FOUND);
        }
    }
}
