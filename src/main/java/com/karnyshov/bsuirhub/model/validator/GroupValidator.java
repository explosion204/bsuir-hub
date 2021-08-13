package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.regex.Pattern;


public class GroupValidator {
    private static final String VALID_NAME = "^[\\p{L}\\d]{1,20}$";

    public static boolean validateGroup(Group group) {
        String name = group.getName();
        return Pattern.matches(VALID_NAME, name);
    }
}
