package com.karnyshov.bsuirhub.controller.filter;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.karnyshov.bsuirhub.controller.command.RequestParameter.STATUS;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code AjaxAccessFilter} class is an implementation of {@link Filter} interface.
 * This filter controls access of users to many ajax URLs like "/ajax/*".
 * @author Dmitry Karnyshov
 */
@WebFilter(filterName = "AjaxAccessFilter")
public class AjaxAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(USER);

        if (user == null || user.getRole() == UserRole.GUEST) {
            Map<String, Object> ajaxResponse = new HashMap<>();
            ajaxResponse.put(STATUS, false);
            String jsonResponse = new Gson().toJson(ajaxResponse);
            httpResponse.getWriter().write(jsonResponse);
        } else {
            chain.doFilter(request, response);
        }
    }
}
