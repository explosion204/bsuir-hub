package com.karnyshov.bsuirhub.util;

import com.karnyshov.bsuirhub.exception.ServiceException;

public interface MailService {
    void sendMail(String recipient, String subject, String body) throws ServiceException;
}
