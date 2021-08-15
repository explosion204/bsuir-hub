package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserRole.GUEST;

/**
 * {@code UserRoleFilter} class is an implementation of {@link Filter} interface.
 * This filter sets {@code user} variable with guest role to new unauthenticated sessions.
 * @author Dmitry Karnyshov
 */
@WebFilter(filterName = "UserRoleFilter")
public class UserRoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(USER);

        if (user == null) {
            user = User.builder().setRole(GUEST).build();
            session.setAttribute(USER, user);
        }

        chain.doFilter(request, response);
    }
}