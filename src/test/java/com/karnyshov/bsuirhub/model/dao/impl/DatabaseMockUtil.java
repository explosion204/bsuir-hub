package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class DatabaseMockUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final String TEST_DB_PROPERTIES_NAME = "test_db.properties";
    private static final String DB_DRIVER_NAME_PROPERTY = "driver";
    private static final String DB_URL_PROPERTY = "url";

    private static final String dbUrl;
    private static final Properties dbProperties = new Properties();

    static {
        ClassLoader classLoader = DatabaseMockUtil.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(TEST_DB_PROPERTIES_NAME)) {
            dbProperties.load(inputStream);

            // register driver
            String driver = dbProperties.getProperty(DB_DRIVER_NAME_PROPERTY);
            Class.forName(driver);

            // retrieve database URL
            dbUrl = dbProperties.getProperty(DB_URL_PROPERTY);
        } catch (IOException e) {
            logger.fatal("Unable to read test database properties", e);
            throw new RuntimeException();
        } catch (ClassNotFoundException e) {
            logger.fatal("Cannot load specified database driver", e);
            throw new RuntimeException();
        }
    }

    public static void mockDatabaseConnectionPool() throws DatabaseConnectionException {
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

        MockedStatic<DatabaseConnectionPool> poolClass = Mockito.mockStatic(DatabaseConnectionPool.class);
        poolClass.when(DatabaseConnectionPool::getInstance).thenReturn(poolInstance);
    }
}
