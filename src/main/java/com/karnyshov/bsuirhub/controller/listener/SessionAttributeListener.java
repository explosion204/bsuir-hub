package com.karnyshov.bsuirhub.controller.listener;

import com.karnyshov.bsuirhub.model.entity.User;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

/**
 * {@code SessionAttributeListener} class is an implementation of {@link HttpSessionAttributeListener} interface.
 * It collects all authenticated sessions.
 * @author Dmitry Karnyshov
 */
@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {
    private static final Map<Long, HttpSession> allSessions = new ConcurrentHashMap<>();

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        HttpSession session = event.getSession();
        String attributeName = event.getName();

        if (attributeName.equals(USER)) {
            User user = (User) session.getAttribute(USER);
            long userId = user.getEntityId();
            allSessions.put(userId, session);
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        String attributeName = event.getName();

        if (attributeName.equals(USER)) {
            User user = (User) event.getValue();
            long userId = user.getEntityId();
            allSessions.remove(userId);
        }
    }

    /**
     * Find an authenticated session by its owner's id.
     *
     * @param userId unique id of ther user.
     * @return {@link HttpSession} instance wrapped with {@link Optional}.
     */
    public static Optional<HttpSession> findSession(long userId) {
        HttpSession session = allSessions.get(userId);

        return session != null ? Optional.of(session) : Optional.empty();
    }
}
