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

/**
 * {@code UserValidator} class contains methods for validation untrusted data encapsulated in {@link User} entity.
 * @author Dmitry Karnyshov
 */
public class UserValidator {
    private static final Logger logger = LogManager.getLogger();

    private static final String VALID_LOGIN_REGEX = "^\\p{Alnum}{8,20}$";
    private static final String VALID_EMAIL_REGEX = "^[\\w.]+@[\\w.]+$";
    private static final String VALID_PASSWORD_REGEX = "^(?=.*\\p{Alpha})(?=.*\\d)[\\p{Alnum}]{8,32}$";
    private static final String VALID_NAME = "^\\p{L}{1,50}$";

    private enum AcceptedImageType {
        JPEG("image/jpeg"),
        PNG("image/png");

        private String mimeType;

        AcceptedImageType(String mimeType) {
            this.mimeType = mimeType;
        }

        private String getMimeType() {
            return mimeType;
        }
    }

    /**
     * Validate {@link User} entity.
     *
     * @param user object that needs validation.
     * @param password plain text password of the user.
     * @param confirmPassword repeated plain text password of the user.
     * @param skipRoleValidation role validation will be skipped if this flag is {@code true}
     * @param skipEmailValidation email validation will be skipped if this flag is {@code true}
     * @param skipPasswordValidation password validation will be skipped if this flag is {@code true}
     * @return {@code true} if the user is valid, {@code false} otherwise.
     */
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

    /**
     * Validate profile image type.
     *
     * @param filePath path to the object of validation.
     * @return {@code true} if the image is PNG or JPEG, {@code false} otherwise.
     */
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

    /**
     * Validate password.
     *
     * @param password plain text password of the user.
     * @param confirmPassword repeated plain text password of the user.
     * @return {@code true} if the email is valid, {@code false} otherwise.
     */
    public static boolean validatePassword(String password, String confirmPassword) {
        boolean validationResult;

        validationResult = Pattern.matches(VALID_PASSWORD_REGEX, password);
        validationResult &= StringUtils.equals(password, confirmPassword);

        return validationResult;
    }

    /**
     * Validate email.
     *
     * @param email email of the user.
     * @return {@code true} if the email is valid, {@code false} otherwise.
     */
    public static boolean validateEmail(String email) {
        return Pattern.matches(VALID_EMAIL_REGEX, email);
    }
}
