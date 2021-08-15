package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.util.UrlStringBuilder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.LOGIN_URL;
import static com.karnyshov.bsuirhub.controller.command.RequestParameter.RETURN_URL;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code SettingsAccessFilter} class is an implementation of {@link Filter} interface.
 * This filter controls access of users to URLs like "/settings/*".
 * @author Dmitry Karnyshov
 */
@WebFilter(filterName = "SettingsAccessFilter")
public class SettingsAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(USER);

        if (user == null || user.getRole() == UserRole.GUEST) {
            String returnUrl = new UrlStringBuilder(httpRequest.getRequestURI()).build();
            session.setAttribute(RETURN_URL, returnUrl);
            httpResponse.sendRedirect(LOGIN_URL);
        } else {
            chain.doFilter(request, response);
        }
    }
}
