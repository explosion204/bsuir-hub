package com.karnyshov.bsuirhub.model.validator;


/**
 * {@code PlainTextValidator} class contains methods for validation untrusted text data.
 * @author Dmitry Karnyshov
 */
public class PlainTextValidator {
    private static final int MIN_TEXT_LENGTH = 1;
    private static final int MAX_TEXT_LENGTH = 255;

    /**
     * Validate plain text.
     *
     * @param text text that needs validation.
     * @return {@code true} if the text is valid, {@code false} otherwise.
     */
    public static boolean validateText(String text) {
        int textLength = text.length();
        return textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH;
    }
}
