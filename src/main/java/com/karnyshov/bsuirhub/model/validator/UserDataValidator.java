package com.karnyshov.bsuirhub.model.validator;

import java.util.regex.Pattern;

public class UserDataValidator {
    private static final String VALID_LOGIN_REGEX = "^\\p{Alnum}{8,20}$";
    private static final String VALID_EMAIL_REGEX = "^[\\p{Alnum}._]+@[\\p{Alnum}._]+$";
    private static final String VALID_PASSWORD_REGEX = "^(?=.*\\w)(?=.*\\d)[\\p{Alnum}]{8,32}$";
    private static final String VALID_NAME = "\\p{L}{1,50}";

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
}
