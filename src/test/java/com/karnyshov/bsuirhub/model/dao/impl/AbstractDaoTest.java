package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractDaoTest {
    private static final String TEST_DB_PROPERTIES_NAME = "test_db.properties";
    private static final String DB_DRIVER_NAME_PROPERTY = "driver";
    private static final String DB_URL_PROPERTY = "url";

    private MockedStatic<DatabaseConnectionPool> mockedPool;

    @BeforeSuite(alwaysRun = true)
    public final void registerConnectionPoolMock() throws IOException, ClassNotFoundException, DatabaseConnectionException {
        String dbUrl;
        Properties dbProperties;

        ClassLoader classLoader = AbstractDaoTest.class.getClassLoader();
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
                e.printStackTrace();
            }

            return connection;
        });

        mockedPool = Mockito.mockStatic(DatabaseConnectionPool.class);
        mockedPool.when(DatabaseConnectionPool::getInstance).thenReturn(poolInstance);
    }

    @AfterSuite(alwaysRun = true)
    public final void deregisterConnectionPoolMock() {
        mockedPool.close();
    }
}
