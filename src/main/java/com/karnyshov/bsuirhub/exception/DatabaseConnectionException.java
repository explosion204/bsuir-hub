package com.karnyshov.bsuirhub.exception;

/**
 * {@code DatabaseConnectionException} class covers exceptions related to database connections.
 * @author Dmitry Karnyshov
 */
public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException() {
        super();
    }
    public DatabaseConnectionException(String message) {
        super(message);
    }
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
    public DatabaseConnectionException(Throwable cause) {
        super(cause);
    }
}
