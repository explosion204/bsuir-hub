package com.karnyshov.bsuirhub.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.DISABLED_COOKIES_JSP;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INDEX_URL;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.LOCALE_CODE;

@WebFilter(filterName = "CookiesFilter")
public class CookiesFilter implements Filter {
    private static final String LOCALE_COOKIE_NAME = "localeCode";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // redirect to error page if cookies are disabled by client
        if (httpRequest.getCookies() == null) {
            httpRequest.getRequestDispatcher(DISABLED_COOKIES_JSP).forward(request, response);
        } else {
            // set new locale
            String localeCode = request.getParameter(LOCALE_CODE);
            if (localeCode != null) {
                Cookie localeCookie = new Cookie(LOCALE_COOKIE_NAME, localeCode);
                // it prevents duplicate cookies with different paths, e.g. / and /login
                localeCookie.setPath(INDEX_URL);
                httpResponse.addCookie(localeCookie);
            }

            chain.doFilter(request, response);
        }
    }
}
