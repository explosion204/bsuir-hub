package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PoolMockExtension implements BeforeAllCallback, AfterAllCallback {
    private static final Logger logger = LogManager.getLogger();
    private static final String TEST_DB_PROPERTIES_NAME = "test_db.properties";
    private static final String DB_DRIVER_NAME_PROPERTY = "driver";
    private static final String DB_URL_PROPERTY = "url";
    private MockedStatic<DatabaseConnectionPool> mockedPool;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String dbUrl;
        Properties dbProperties;

        ClassLoader classLoader = PoolMockExtension.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(TEST_DB_PROPERTIES_NAME)) {
            dbProperties = new Properties();
            dbProperties.load(inputStream);

            // register driver
            String driver = dbProperties.getProperty(DB_DRIVER_NAME_PROPERTY);
            Class.forName(driver);

            // retrieve database URL
            dbUrl = dbProperties.getProperty(DB_URL_PROPERTY);
        }

        DatabaseConnectionPool poolInstance = Mockito.mock(DatabaseConnectionPool.class);
        Mockito.when(poolInstance.acquireConnection()).thenAnswer(invocation -> {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(dbUrl, dbProperties);
            } catch (SQLException e) {
                logger.error(e);
            }

            return connection;
        });

        mockedPool = Mockito.mockStatic(DatabaseConnectionPool.class);
        mockedPool.when(DatabaseConnectionPool::getInstance).thenReturn(poolInstance);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        mockedPool.close();
    }
}
