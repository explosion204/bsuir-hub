package com.karnyshov.bsuirhub.controller.filter;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.AuditEntityDao;
import com.karnyshov.bsuirhub.model.entity.AuditEntity;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;
import static java.time.ZoneOffset.UTC;

@WebFilter(filterName = "AuditFilter")
public class AuditFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private AuditEntityDao auditDao;

    @Inject
    public AuditFilter(AuditEntityDao auditDao) {
        this.auditDao = auditDao;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(USER);

        if (user != null && user.getRole() != UserRole.GUEST) {
            long userId = user.getEntityId();
            String url = httpRequest.getRequestURI();
            LocalDateTime timestamp = LocalDateTime.now(UTC);
            AuditEntity auditEntity = (AuditEntity) AuditEntity.builder()
                    .setUserId(userId)
                    .setRequest(url)
                    .setTimestamp(timestamp)
                    .build();

            try {
                auditDao.insert(auditEntity);
            } catch (DaoException e) {
                logger.error("Unable to log user action, skipping");
            }

            logger.error("user: {}, request: {}, timestamp: {}", userId, url, timestamp);
        }

        chain.doFilter(request, response);
    }
}
