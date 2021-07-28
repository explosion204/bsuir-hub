package com.karnyshov.bsuirhub.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public interface JwtService {
    String ID_CLAIM = "id";
    String EMAIL_CLAIM = "email";

    String generateJwt(long userId, String email);
    Optional<Pair<Long, String>> parseJwt(String token);
}
