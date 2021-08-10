package com.karnyshov.bsuirhub.controller.command.validator;

import com.karnyshov.bsuirhub.model.entity.Comment;
import jakarta.inject.Named;

@Named
public class CommentValidator {
    private static final int MIN_TEXT_LENGTH = 1;
    private static final int MAX_TEXT_LENGTH = 255;

    public boolean validateComment(Comment comment) {
        int textLength = comment.getText().length();
        return textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH;
    }
}
