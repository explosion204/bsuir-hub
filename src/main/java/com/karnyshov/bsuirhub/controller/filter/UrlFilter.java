package com.karnyshov.bsuirhub.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.RequestAttribute.ORIGINAL_URL;

@WebFilter(
        urlPatterns = { "/*" }
)
public class UrlFilter implements Filter {
    private static final String MAIN_CONTROLLER_URL = "/main";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = httpRequest.getRequestURI();

        httpRequest.setAttribute(ORIGINAL_URL, url);
        httpRequest.getRequestDispatcher(MAIN_CONTROLLER_URL).forward(httpRequest, httpResponse);

        if (chain != null) {
            chain.doFilter(request, response);
        }
    }
}
