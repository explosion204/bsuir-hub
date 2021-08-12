package com.karnyshov.bsuirhub.util;

import java.util.Map;

public interface TokenService {
    String generateToken(Map<String, Object> claims);
    Map<String, Object> parseToken(String token);
}
