package com.karnyshov.bsuirhub.model.pool;

import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * {@code ConnectionFactory} class is used for internal creating connections via JDBC driver.
 * @author Dmitry Karnyshov
 */
class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger();

    private static final String DB_PROPERTIES_NAME = "db.properties";
    private static final String DB_DRIVER_NAME_PROPERTY = "driver";
    private static final String DB_URL_PROPERTY = "url";

    private static final String dbUrl;
    private static final Properties dbProperties = new Properties();

    static {
        ClassLoader classLoader = ConnectionFactory.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(DB_PROPERTIES_NAME)) {
            dbProperties.load(inputStream);

            // register driver
            String driver = dbProperties.getProperty(DB_DRIVER_NAME_PROPERTY);
            Class.forName(driver);

            // retrieve database URL
            dbUrl = dbProperties.getProperty(DB_URL_PROPERTY);
        } catch (IOException e) {
            logger.fatal("Unable to read database properties", e);
            throw new RuntimeException();
        } catch (ClassNotFoundException e) {
            logger.fatal("Cannot load specified database driver", e);
            throw new RuntimeException();
        }
    }

    /**
     * Create a database connection. {@link Connection} implementation is a {@link ProxyConnection} under the hood.
     *
     * @return {@code Connection} instance.
     * @throws DatabaseConnectionException if application is unable to establish proper connection with database.
     * @see ProxyConnection
     */
    static Connection createConnection() throws DatabaseConnectionException {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbProperties);
            return new ProxyConnection(connection);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Unable to establish connection", e);
        }
    }
}
