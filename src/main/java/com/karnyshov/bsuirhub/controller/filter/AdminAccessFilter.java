package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.controller.command.SessionAttribute;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import jakarta.servlet.*;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * {@code AdminAccessFilter} class is an implementation of {@link Filter} interface.
 * This filter controls access of users to URLs like "/admin/*".
 * @author Dmitry Karnyshov
 */
@WebFilter(filterName = "AdminAccessFilter")
public class AdminAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(SessionAttribute.USER);

        if (user == null || user.getRole() != UserRole.ADMIN) {
            httpResponse.sendError(SC_NOT_FOUND);
        } else {
            chain.doFilter(request, response);
        }
    }
}
