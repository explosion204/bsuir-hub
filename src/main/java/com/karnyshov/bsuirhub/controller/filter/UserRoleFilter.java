package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.controller.command.SessionAttribute;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(
        urlPatterns = "/main",
        dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD }
)
public class UserRoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(SessionAttribute.USER);

        if (user == null) {
            user = User.builder().setUserRole(UserRole.GUEST).build();
            session.setAttribute(SessionAttribute.USER, user);
        }

        chain.doFilter(request, response);
    }
}
