package com.karnyshov.bsuirhub.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;

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
    private static final String JSESSION_ID_REGEX = ".*;jsessionid=.*";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        Cookie[] cookies = httpRequest.getCookies();
        String requestedUrl = httpRequest.getRequestURI();

        if (cookies == null) {
            // no cookies, it might be the first request to application, so we have to make a little check
            if (session.getAttribute(COOKIE_CHECK) != null) {
                // if it were "check" request to ensure that server is able to set cookies
                // and cookies are still null (i.e. disabled by client), then forward to error page
                request.getRequestDispatcher(DISABLED_COOKIES_JSP).forward(request, response);
            } else {
                // if it is not a "check" request and cookies are null, then set cookie with default locale and
                // redirect user to the index page but with session COOKIE_CHECK flag.
                session.setAttribute(COOKIE_CHECK, COOKIE_CHECK);
                // encode url in case when cookies might be disabled
                String encodedUrl = httpResponse.encodeRedirectURL(requestedUrl);
                httpResponse.sendRedirect(encodedUrl);
            }
        } else {
            if (httpRequest.getRequestURI().contains(JSESSION_ID_SUBSTRING)) {
                // if URL contains jsessionid we have to purify it through redirect
                session.removeAttribute(COOKIE_CHECK);
                // remove jsessionid from url
                requestedUrl = requestedUrl.replaceAll(JSESSION_ID_REGEX, StringUtils.EMPTY);
                httpResponse.sendRedirect(requestedUrl);
            } else if (!cookieIsPresent(cookies, LOCALE_COOKIE_NAME)) {
                // locale cookie must be set to default in case if it is not present
                // (it might be first request or user deleted it intentionally)
                Cookie localeCookie = new Cookie(LOCALE_COOKIE_NAME, DEFAULT_LOCALE);
                localeCookie.setPath(INDEX_URL);
                httpResponse.addCookie(localeCookie);
                httpResponse.sendRedirect(requestedUrl);
            } else {
                // otherwise, we continue usual flow (and set new locale if locale code can be found as request param)
                String localeCode = request.getParameter(LOCALE_CODE);

                if (localeCode != null) {
                    Cookie localeCookie = new Cookie(LOCALE_COOKIE_NAME, localeCode);
                    localeCookie.setPath(INDEX_URL);
                    httpResponse.addCookie(localeCookie);
                }

                chain.doFilter(request, response);
            }
        }
    }

    private boolean cookieIsPresent(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals(cookieName));
    }
}
