package com.karnyshov.bsuirhub.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public interface TokenService {
    String ID_CLAIM = "id";
    String EMAIL_CLAIM = "email";
    String SALT_CLAIM = "salt";

    String generateEmailConfirmationToken(long userId, String email);
    String generatePasswordResetToken(long userId, String salt);
    Optional<Pair<Long, String>> parseEmailConfirmationToken(String token);
    Optional<Pair<Long, String>> parsePasswordResetToken(String token);
}
