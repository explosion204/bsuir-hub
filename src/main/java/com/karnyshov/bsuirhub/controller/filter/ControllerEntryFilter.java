package com.karnyshov.bsuirhub.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;


@WebFilter(filterName = "ControllerEntryFilter")
public class ControllerEntryFilter implements Filter {
    private static final String MAIN_CONTROLLER_URL = "/controller";
    private static final String CACHE_CONTROL_HEADER = "Cache-Control";
    private static final String CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";
    private static final String PRAGMA_HEADER = "Pragma";
    private static final String PRAGMA_VALUE = "no-cache";
    private static final String EXPIRES_HEADER = "Expires";
    private static final String EXPIRES_VALUE = "0";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();
        httpRequest.setAttribute(ORIGINAL_URL, url);

        disableCaching(httpResponse);
        extractAlertsFromSession(httpRequest);
        httpRequest.getRequestDispatcher(MAIN_CONTROLLER_URL).forward(httpRequest, httpResponse);
    }

    private void disableCaching(HttpServletResponse response) {
        response.setHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE); // HTTP 1.1.
        response.setHeader(PRAGMA_HEADER, PRAGMA_VALUE); // HTTP 1.0.
        response.setHeader(EXPIRES_HEADER, EXPIRES_VALUE); // Proxies.
    }

    private void extractAlertsFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // operation success
        request.setAttribute(ENTITY_UPDATE_SUCCESS, session.getAttribute(ENTITY_UPDATE_SUCCESS));
        request.setAttribute(EMAIL_CHANGE_SUCCESS, session.getAttribute(EMAIL_CHANGE_SUCCESS));
        request.setAttribute(PASSWORD_CHANGE_SUCCESS, session.getAttribute(PASSWORD_CHANGE_SUCCESS));
        request.setAttribute(EMAIL_CONFIRMATION_SUCCESS, session.getAttribute(EMAIL_CONFIRMATION_SUCCESS));
        request.setAttribute(PASSWORD_RESET_LINK_SENT, session.getAttribute(PASSWORD_RESET_LINK_SENT));
        request.setAttribute(MESSAGE_SENT, session.getAttribute(MESSAGE_SENT));

        session.removeAttribute(ENTITY_UPDATE_SUCCESS);
        session.removeAttribute(EMAIL_CHANGE_SUCCESS);
        session.removeAttribute(PASSWORD_CHANGE_SUCCESS);
        session.removeAttribute(EMAIL_CONFIRMATION_SUCCESS);
        session.removeAttribute(PASSWORD_RESET_LINK_SENT);
        session.removeAttribute(MESSAGE_SENT);

        // auth error
        request.setAttribute(AUTH_ERROR, session.getAttribute(AUTH_ERROR));
        session.removeAttribute(AUTH_ERROR);

        // validation errors
        request.setAttribute(VALIDATION_ERROR, session.getAttribute(VALIDATION_ERROR));
        request.setAttribute(NOT_UNIQUE_LOGIN, session.getAttribute(NOT_UNIQUE_LOGIN));
        request.setAttribute(NOT_UNIQUE_EMAIL, session.getAttribute(NOT_UNIQUE_EMAIL));
        request.setAttribute(INVALID_CURRENT_PASSWORD, session.getAttribute(INVALID_CURRENT_PASSWORD));
        request.setAttribute(NOT_UNIQUE_NAME, session.getAttribute(NOT_UNIQUE_NAME));

        session.removeAttribute(VALIDATION_ERROR);
        session.removeAttribute(NOT_UNIQUE_LOGIN);
        session.removeAttribute(NOT_UNIQUE_EMAIL);
        session.removeAttribute(INVALID_CURRENT_PASSWORD);
        session.removeAttribute(NOT_UNIQUE_NAME);
    }
}
