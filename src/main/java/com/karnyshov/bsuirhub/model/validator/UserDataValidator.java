package com.karnyshov.bsuirhub.model.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Pattern;


public class UserDataValidator {
    private static final Logger logger = LogManager.getLogger();

    private static final String VALID_LOGIN_REGEX = "^\\p{Alnum}{8,20}$";
    private static final String VALID_EMAIL_REGEX = "^[\\p{Alnum}._]+@[\\p{Alnum}._]+$";
    private static final String VALID_PASSWORD_REGEX = "^(?=.*\\w)(?=.*\\d)[\\p{Alnum}]{8,32}$";
    private static final String VALID_NAME = "\\p{L}{1,50}";

    private enum AcceptedImageType {
        JPEG("image/jpeg"), PNG("image/png");

        private String mimeType;

        AcceptedImageType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getMimeType() {
            return mimeType;
        }
    }

    public static boolean validateLogin(String login) {
        return Pattern.matches(VALID_LOGIN_REGEX, login);
    }

    public static boolean validateEmail(String email) {
        return Pattern.matches(VALID_EMAIL_REGEX, email);
    }

    public static boolean validateName(String name) {
        return Pattern.matches(VALID_NAME, name);
    }

    public static boolean validatePassword(String password) {
        return Pattern.matches(VALID_PASSWORD_REGEX, password);
    }

    public static boolean validateProfileImage(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Tika tika = new Tika();
            String mimeType = tika.detect(inputStream);
            return Arrays.stream(AcceptedImageType.values())
                    .anyMatch(mt -> mt.getMimeType().equals(mimeType));
        } catch (IOException e) {
            logger.error("File not found: " + filePath);
            return false;
        }
    }
}
