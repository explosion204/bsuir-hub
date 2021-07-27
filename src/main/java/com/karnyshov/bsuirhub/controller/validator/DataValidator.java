package com.karnyshov.bsuirhub.controller.validator;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.AlertAttribute.*;

@Named
@Singleton
public class DataValidator {
    private static final Logger logger = LogManager.getLogger();

    private static final String VALID_LOGIN_REGEX = "^\\p{Alnum}{8,20}$";
    private static final String VALID_EMAIL_REGEX = "^[\\p{Alnum}._]+@[\\p{Alnum}._]+$";
    private static final String VALID_PASSWORD_REGEX = "^(?=.*\\w)(?=.*\\d)[\\p{Alnum}]{8,32}$";
    private static final String VALID_NAME = "\\p{L}{1,50}";

    @Inject
    private UserService userService;

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


    public boolean validateProfileImage(String filePath) {
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

    public boolean validateUser(HttpServletRequest request, User user, String idString, String cachedEmail,
                                UserRole cachedRole, String password, String confirmPassword) {
        HttpSession session = request.getSession();

        boolean mainValidationResult;
        boolean minorValidationResult;
        UserRole role = user.getUserRole();
        String login = user.getLogin();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();
        String lastName = user.getLastName();

        // this makes impossible to grant administrator role without direct access to database
        minorValidationResult = role == cachedRole || role != UserRole.ADMIN;
        mainValidationResult = minorValidationResult;
        session.setAttribute(INVALID_ROLE, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_LOGIN_REGEX, login);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_LOGIN, !minorValidationResult);

        // update user -> ignore login uniqueness validation because dao ignores login anyways
        minorValidationResult = idString != null || userService.isLoginUnique(login);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(NOT_UNIQUE_LOGIN, !minorValidationResult);

        minorValidationResult = StringUtils.isBlank(email) || Pattern.matches(VALID_EMAIL_REGEX, email);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_EMAIL, !minorValidationResult);

        // update user -> ignore email uniqueness validation if email is not changed or empty
        minorValidationResult = idString != null && Objects.equals(email, cachedEmail) || StringUtils.isBlank(email)
                || userService.isEmailUnique(email);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(NOT_UNIQUE_EMAIL, !minorValidationResult);

        // password validation is ignored (case - update user leaving password fields empty)
        minorValidationResult = idString != null && StringUtils.isBlank(password)
                || Pattern.matches(VALID_PASSWORD_REGEX, password);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_PASSWORD, !minorValidationResult);

        minorValidationResult = idString != null && StringUtils.isBlank(password)
                || StringUtils.equals(password, confirmPassword);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(PASSWORDS_DO_NOT_MATCH, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_NAME, firstName);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_FIRST_NAME, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_NAME, patronymic);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_PATRONYMIC, !minorValidationResult);

        minorValidationResult = Pattern.matches(VALID_NAME, lastName);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_LAST_NAME, !minorValidationResult);

        return mainValidationResult;
    }

    public boolean validateUser(HttpServletRequest request, User user, String password, String confirmPassword) {
        return validateUser(request, user, null, null, null, password, confirmPassword);
    }

    public boolean validatePasswordChange(HttpServletRequest request, long userId, String currentPassword,
                String password, String confirmPassword) {
        HttpSession session = request.getSession();

        boolean mainValidationResult;
        boolean minorValidationResult;

        Optional<User> targetUser;

        try {
            targetUser = userService.findById(userId);
        } catch (ServiceException e) {
            logger.error("Something went wrong trying to find user with id " + userId);
            return false;
        }

        if (targetUser.isEmpty()) {
            logger.error("Unable to find user with id " + userId);
            return false;
        }

        String currentPasswordHash = targetUser.get().getPasswordHash();
        String currentSalt = targetUser.get().getSalt();

        minorValidationResult = StringUtils.equals(currentPasswordHash,
                DigestUtils.sha256Hex(currentPassword + currentSalt));
        mainValidationResult = minorValidationResult;
        session.setAttribute(INVALID_CURRENT_PASSWORD, !minorValidationResult);

        minorValidationResult = StringUtils.isBlank(password)
                || Pattern.matches(VALID_PASSWORD_REGEX, password);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(INVALID_PASSWORD, !minorValidationResult);

        minorValidationResult = StringUtils.isBlank(password)
                || StringUtils.equals(password, confirmPassword);
        mainValidationResult &= minorValidationResult;
        session.setAttribute(PASSWORDS_DO_NOT_MATCH, !minorValidationResult);

        return mainValidationResult;
    }
}
