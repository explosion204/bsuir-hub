package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.regex.Pattern;

/**
 * {@code GroupValidator} class contains methods for validation untrusted data encapsulated in {@link Group} entity.
 * @author Dmitry Karnyshov
 */
public class GroupValidator {
    private static final String VALID_NAME = "^[\\p{L}\\d]{1,20}$";

    /**
     * Validate {@link Group} entity.
     *
     * @param group object that needs validation.
     * @return {@code true} if the group is valid, {@code false} otherwise.
     */
    public static boolean validateGroup(Group group) {
        String name = group.getName();
        return Pattern.matches(VALID_NAME, name);
    }
}
