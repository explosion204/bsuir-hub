package com.karnyshov.bsuirhub.util.impl;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.util.MailService;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@code MailServiceImpl} class is an implementation of {@link MailService} interface.
 * Main feature of this implementation is sending emails in separate threads from fixed thread pool.
 * Pool size is defined in configuration .properties file.
 * @author Dmitry Karnyshov
 */
public class MailServiceImpl implements MailService {
    private static final Logger logger = LogManager.getLogger();
    private static final String MAIL_PROPERTIES_NAME = "mail.properties";
    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String THREAD_POOL_SIZE_PROPERTY = "thread_pool_size";
    private static final String HTML_BODY_TYPE = "text/html; charset=UTF-8";

    private static Properties mailProperties;
    private static Session mailSession;
    private static String sender;

    private static ExecutorService emailExecutor;

    static {
        ClassLoader classLoader = MailServiceImpl.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(MAIL_PROPERTIES_NAME)) {
            mailProperties = new Properties();
            mailProperties.load(inputStream);
            sender = mailProperties.getProperty(USERNAME_PROPERTY);
            String password = mailProperties.getProperty(PASSWORD_PROPERTY);

            mailSession = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });

            int threadPoolSize = Integer.parseInt(mailProperties.getProperty(THREAD_POOL_SIZE_PROPERTY));
            emailExecutor = Executors.newFixedThreadPool(threadPoolSize);
        } catch (IOException e) {
            logger.fatal("Unable to read mailing properties", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMail(String recipient, String subject, String body) throws ServiceException {
        Message message = new MimeMessage(mailSession);

        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, HTML_BODY_TYPE);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            emailExecutor.execute(() -> {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                    logger.error("An error occurred while sending message: ", e);
                }
            });
        } catch (MessagingException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public String getMailProperty(String propertyName) {
        return mailProperties.getProperty(propertyName);
    }
}
