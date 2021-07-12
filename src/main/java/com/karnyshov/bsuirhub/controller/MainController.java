package com.karnyshov.bsuirhub.controller;

import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.command.CommandType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/bsuirhub")
public class MainController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(RequestParameter.COMMAND);
        Command command = CommandType.getCommand(commandName);
        CommandResult commandResult = command.execute(request);

        String routePath = commandResult.getRoutePath();
        CommandResult.RouteType routeType = commandResult.getRouteType();

        switch (routeType) {
            case FORWARD:
                request.getRequestDispatcher(routePath).forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(routePath);
                break;
            default:
                logger.error("Invalid route type: " + routeType.name());
                // TODO: 7/12/2021 error pages
        }
    }
}
