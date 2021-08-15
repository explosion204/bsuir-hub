package com.karnyshov.bsuirhub.exception;

/**
 * {@code DaoException} class is DAO layer exception.
 * @author Dmitry Karnyshov
 */
public class DaoException extends Exception {
    public DaoException() {
        super();
    }
    public DaoException(String message) {
        super(message);
    }
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
    public DaoException(Throwable cause) {
        super(cause);
    }
}
