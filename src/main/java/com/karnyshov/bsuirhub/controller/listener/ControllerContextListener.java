package com.karnyshov.bsuirhub.controller.listener;

import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ControllerContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // destroy pool and release all database connections
        DatabaseConnectionPool.getInstance().destroyPool();
    }
}
