package com.karnyshov.bsuirhub.util;

import java.util.Map;

/**
 * {@code TokenService} interface provides functionality for generating and parsing tokens.
 * @author Dmitry Karnyshov
 */
public interface TokenService {
    /**
     * Generate token.
     *
     * @param claims {@link Map} of key-value pairs that will be embedded to the token.
     * @return stringified token.
     */
    String generateToken(Map<String, Object> claims);

    /**
     * Check token validity and parse it.
     *
     * @param token stringified token.
     * @return {@link Map} of key-value pairs that are embedded to the token. If the token is invalid,
     * empty map is returned.
     */
    Map<String, Object> parseToken(String token);
}
