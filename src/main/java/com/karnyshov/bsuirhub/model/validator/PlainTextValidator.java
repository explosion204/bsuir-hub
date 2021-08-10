package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Comment;

public class PlainTextValidator {
    private static final int MIN_TEXT_LENGTH = 1;
    private static final int MAX_TEXT_LENGTH = 255;

    public static boolean validateText(String text) {
        int textLength = text.length();
        return textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH;
    }
}
