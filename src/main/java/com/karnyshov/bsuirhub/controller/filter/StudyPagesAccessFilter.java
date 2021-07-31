package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.LOGIN_URL;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.SETTINGS_URL;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static com.karnyshov.bsuirhub.model.entity.UserRole.GUEST;
import static com.karnyshov.bsuirhub.model.entity.UserStatus.NOT_CONFIRMED;

@WebFilter(
        urlPatterns = "/study/*" // TODO: 7/28/2021
)
public class StudyPagesAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(USER);
        
        if (user == null || user.getRole() == GUEST) {
            httpResponse.sendRedirect(LOGIN_URL);
        } else if (user.getStatus() == NOT_CONFIRMED) {
            httpResponse.sendRedirect(SETTINGS_URL);
        } else {
            chain.doFilter(request, response);
        }
    }
}
