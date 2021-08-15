package com.karnyshov.bsuirhub.exception;

/**
 * {@code ServiceException} class is Service layer exception.
 * @author Dmitry Karnyshov
 */
public class ServiceException extends Exception {
    public ServiceException() {
        super();
    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
