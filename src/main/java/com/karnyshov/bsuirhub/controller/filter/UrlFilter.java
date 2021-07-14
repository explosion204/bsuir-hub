package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.controller.MainController;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;

@WebFilter(urlPatterns = { "/*" })
public class UrlFilter implements Filter {
    private static final String MAIN_CONTROLLER_URL = "/app";
    private static final String JSP_REGEX = "\\/pages\\/.+\\.jsp";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = httpRequest.getRequestURI();

        if (Pattern.matches(JSP_REGEX, url)) {
            httpRequest.getRequestDispatcher(url).forward(httpRequest, httpResponse);
        } else {
            httpRequest.setAttribute(ORIGINAL_URL, url);
            httpRequest.getRequestDispatcher(MAIN_CONTROLLER_URL).forward(httpRequest, httpResponse);
        }
    }
}
