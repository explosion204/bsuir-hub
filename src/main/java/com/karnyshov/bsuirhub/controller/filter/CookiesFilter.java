package com.karnyshov.bsuirhub.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.DISABLED_COOKIES_JSP;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.INDEX_URL;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.LOCALE_CODE;

/**
 * {@code CookiesFilter} class is an implementation of {@link Filter} interface.
 * This filter prevents application usage with disabled cookies.
 * If application is not working without cookies in a proper way, it is a good solution to restrict
 * inconvenient user-experience at all.
 * Also, it sets a new locale as a cookie if a "localeCode" parameter is passed with request.
 * @author Dmitry Karnyshov
 */
@WebFilter(filterName = "CookiesFilter")
public class CookiesFilter implements Filter {
    private static final String LOCALE_COOKIE_NAME = "localeCode";
    private static final String COOKIE_CHECK = "cookieCheck";
    private static final String DEFAULT_LOCALE = "ru";
    private static final String JSESSION_ID_SUBSTRING = "jsessionid";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        Cookie[] cookies = httpRequest.getCookies();

        if (cookies == null) {
            // no cookies, it might be the first request to application, so we have to make a little check
            if (session.getAttribute(COOKIE_CHECK) != null) {
                // if it were "check" request to ensure that server is able to set cookies
                // and cookies are still null (i.e. disabled by client), then forward to error page
                httpRequest.getRequestDispatcher(DISABLED_COOKIES_JSP).forward(request, response);
            } else {
                // if it is not a "check" request and cookies are null, then set cookie with default locale and
                // redirect user to the index page but with session COOKIE_CHECK flag.
                session.setAttribute(COOKIE_CHECK, COOKIE_CHECK);
                httpResponse.addCookie(new Cookie(LOCALE_COOKIE_NAME, DEFAULT_LOCALE));
                // encode url in case when cookies might be disabled
                String encodedUrl = httpResponse.encodeRedirectURL(INDEX_URL);
                httpResponse.sendRedirect(encodedUrl);
            }
        } else {
            // if URL contains jsessionid we have to purify it through redirect
            if (httpRequest.getRequestURI().contains(JSESSION_ID_SUBSTRING)) {
                session.removeAttribute(COOKIE_CHECK);
                httpResponse.sendRedirect(INDEX_URL);
            } else {
                // otherwise, we continue usual flow and set new locale
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
}
