package com.karnyshov.bsuirhub.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;
import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;

@WebFilter(
        urlPatterns = "/*"
)
public class UrlFilter implements Filter {
    private static final String MAIN_CONTROLLER_URL = "/controller";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();
        httpRequest.setAttribute(ORIGINAL_URL, url);

        extractAlertsFromSession(httpRequest);
        httpRequest.getRequestDispatcher(MAIN_CONTROLLER_URL).forward(httpRequest, httpResponse);
    }

    private void extractAlertsFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // operation success
        request.setAttribute(ENTITY_UPDATE_SUCCESS, session.getAttribute(ENTITY_UPDATE_SUCCESS));
        request.setAttribute(EMAIL_CHANGE_SUCCESS, session.getAttribute(EMAIL_CHANGE_SUCCESS));
        request.setAttribute(PASSWORD_CHANGE_SUCCESS, session.getAttribute(PASSWORD_CHANGE_SUCCESS));
        request.setAttribute(EMAIL_CONFIRMATION_SUCCESS, session.getAttribute(EMAIL_CONFIRMATION_SUCCESS));

        session.removeAttribute(ENTITY_UPDATE_SUCCESS);
        session.removeAttribute(EMAIL_CHANGE_SUCCESS);
        session.removeAttribute(PASSWORD_CHANGE_SUCCESS);

        // auth error
        request.setAttribute(AUTH_ERROR, session.getAttribute(AUTH_ERROR));
        session.removeAttribute(AUTH_ERROR);

        // validation errors
        request.setAttribute(INVALID_ROLE, session.getAttribute(INVALID_ROLE));
        request.setAttribute(INVALID_LOGIN, session.getAttribute(INVALID_LOGIN));
        request.setAttribute(NOT_UNIQUE_LOGIN, session.getAttribute(NOT_UNIQUE_LOGIN));
        request.setAttribute(INVALID_EMAIL, session.getAttribute(INVALID_EMAIL));
        request.setAttribute(NOT_UNIQUE_EMAIL, session.getAttribute(NOT_UNIQUE_EMAIL));
        request.setAttribute(INVALID_PASSWORD, session.getAttribute(INVALID_PASSWORD));
        request.setAttribute(INVALID_CURRENT_PASSWORD, session.getAttribute(INVALID_CURRENT_PASSWORD));
        request.setAttribute(PASSWORDS_DO_NOT_MATCH, session.getAttribute(PASSWORDS_DO_NOT_MATCH));
        request.setAttribute(INVALID_FIRST_NAME, session.getAttribute(INVALID_FIRST_NAME));
        request.setAttribute(INVALID_PATRONYMIC, session.getAttribute(INVALID_PATRONYMIC));
        request.setAttribute(INVALID_LAST_NAME, session.getAttribute(INVALID_LAST_NAME));
        request.setAttribute(INVALID_NAME, session.getAttribute(INVALID_NAME));
        request.setAttribute(INVALID_SHORT_NAME, session.getAttribute(INVALID_SHORT_NAME));
        request.setAttribute(NOT_UNIQUE_NAME, session.getAttribute(NOT_UNIQUE_NAME));

        session.removeAttribute(INVALID_ROLE);
        session.removeAttribute(INVALID_LOGIN);
        session.removeAttribute(NOT_UNIQUE_LOGIN);
        session.removeAttribute(INVALID_EMAIL);
        session.removeAttribute(NOT_UNIQUE_EMAIL);
        session.removeAttribute(INVALID_PASSWORD);
        session.removeAttribute(INVALID_CURRENT_PASSWORD);
        session.removeAttribute(PASSWORDS_DO_NOT_MATCH);
        session.removeAttribute(INVALID_FIRST_NAME);
        session.removeAttribute(INVALID_PATRONYMIC);
        session.removeAttribute(INVALID_LAST_NAME);
        session.removeAttribute(INVALID_NAME);
        session.removeAttribute(INVALID_SHORT_NAME);
        session.removeAttribute(NOT_UNIQUE_NAME);
    }
}
