package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.controller.command.ApplicationPath;
import com.karnyshov.bsuirhub.controller.command.SessionAttribute;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(
        urlPatterns = "/admin/*"
)
public class AdminAreaFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(SessionAttribute.USER);

        if (user != null && user.getUserRole() != UserRole.ADMIN) {
            httpResponse.sendRedirect(ApplicationPath.NOT_FOUND_ERROR_URL);
        }

        chain.doFilter(request, response);
    }
}
