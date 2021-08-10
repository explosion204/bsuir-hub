package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Pattern;


public class NewUserValidator {
    private static final Logger logger = LogManager.getLogger();

    private static final String VALID_LOGIN_REGEX = "^\\p{Alnum}{8,20}$";
    private static final String VALID_EMAIL_REGEX = "^[\\p{Alnum}._]+@[\\p{Alnum}._]+$";
    private static final String VALID_PASSWORD_REGEX = "^(?=.*\\w)(?=.*\\d)[\\p{Alnum}]{8,32}$";
    private static final String VALID_NAME = "^\\p{L}{1,50}$";

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

    public static boolean validateUser(User user, String password, String confirmPassword,
                                boolean skipRoleValidation, boolean skipEmailValidation, boolean skipPasswordValidation) {
        boolean validationResult;
        UserRole role = user.getRole();
        String login = user.getLogin();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();
        String lastName = user.getLastName();

        validationResult = skipRoleValidation || role != UserRole.ADMIN;
        validationResult &= Pattern.matches(VALID_LOGIN_REGEX, login);
        validationResult &= skipEmailValidation || StringUtils.isBlank(email) || validateEmail(email);
        validationResult &= skipPasswordValidation || validatePassword(password, confirmPassword);
        validationResult &= Pattern.matches(VALID_NAME, firstName);
        validationResult &= Pattern.matches(VALID_NAME, patronymic);
        validationResult &= Pattern.matches(VALID_NAME, lastName);

        return validationResult;
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

    public static boolean validatePassword(String password, String confirmPassword) {
        boolean validationResult;

        validationResult = Pattern.matches(VALID_PASSWORD_REGEX, password);
        validationResult &= StringUtils.equals(password, confirmPassword);

        return validationResult;
    }

    public static boolean validateEmail(String email) {
        return Pattern.matches(VALID_EMAIL_REGEX, email);
    }
}
