package com.karnyshov.bsuirhub.util;

import com.karnyshov.bsuirhub.exception.ServiceException;

/**
 * {@code MailService} interface provides sending email functionality.
 * @author Dmitry Karnyshov
 */
public interface MailService {
    /**
     * Send email.
     *
     * @param recipient receiver of the email.
     * @param subject subject of the email.
     * @param body body of the email.
     * @throws ServiceException if an error occurred executing the method.
     */
    void sendMail(String recipient, String subject, String body) throws ServiceException;

    /**
     * Get a string value associated with provided key from the mail configuration.
     *
     * @param propertyName key in the mail configuration.
     * @return string value associated with provided key.
     */
    String getMailProperty(String propertyName);
}
