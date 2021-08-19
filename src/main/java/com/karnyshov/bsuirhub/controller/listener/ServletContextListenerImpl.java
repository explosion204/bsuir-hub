package com.karnyshov.bsuirhub.controller.listener;

import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * {@code ServletContextListenerImpl} class is an implementation of {@link jakarta.servlet.ServletContextListener} interface.
 * It detects the moment when the application controller has been destroyed and then destroys
 * connection pool.
 * @author Dmitry Karnyshov
 */
@WebListener
public class ServletContextListenerImpl implements jakarta.servlet.ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // destroy pool and release all database connections
        DatabaseConnectionPool.getInstance().destroyPool();
    }
}
