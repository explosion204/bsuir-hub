package com.karnyshov.bsuirhub.model.validator;

import java.util.regex.Pattern;

public class UserValidator {
    private static final String VALID_LOGIN_REGEX = "^\\p{Alnum}{8,20}$";
    private static final String VALID_PASSWORD_REGEX = "^(?=.*\\w)(?=.*\\d)[\\p{Alnum}]{8,32}$";

    public static boolean validateLogin(String login) {
        return Pattern.matches(VALID_LOGIN_REGEX, login);
    }

    public static boolean validatePassword(String password) {
        return Pattern.matches(VALID_PASSWORD_REGEX, password);
    }
}
