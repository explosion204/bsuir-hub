package com.karnyshov.bsuirhub.util.impl;

import com.karnyshov.bsuirhub.util.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * {@code TokenServiceImpl} class is an implementation of {@link TokenService} interfaces.
 * This implementation provides functionality of generating, validation and parsing
 * JSON Web Tokens.
 * @author Dmitry Karnyshov
 */
public class TokenServiceImpl implements TokenService {
    private static final Logger logger = LogManager.getLogger();
    private static final String JWT_PROPERTIES_NAME = "jwt.properties";
    private static final String SECRET_KEY_PROPERTY = "secret_key";
    private static final String VALIDITY_TIME_PROPERTY = "validity_time";

    private static Key secretKey;
    private static int validityTime; // measured in hours

    static {
        ClassLoader classLoader = MailServiceImpl.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(JWT_PROPERTIES_NAME)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            validityTime = Integer.parseInt(properties.getProperty(VALIDITY_TIME_PROPERTY));

            String rawKey = properties.getProperty(SECRET_KEY_PROPERTY);
            secretKey = Keys.hmacShaKeyFor(rawKey.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.fatal("Unable to read jwt properties", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateToken(Map<String, Object> claims) {
        Instant expirationInstant = LocalDateTime.now(Clock.systemUTC())
                .plus(validityTime, ChronoUnit.HOURS)
                .toInstant(ZoneOffset.UTC);
        Date expirationTime = Date.from(expirationInstant);

        JwtBuilder builder = Jwts.builder()
                .setExpiration(expirationTime)
                .signWith(secretKey);
        claims.forEach(builder::claim);

        return builder.compact();
    }

    @Override
    public Map<String, Object> parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new HashMap<>(claims);

        } catch (JwtException | NumberFormatException e) {
            logger.error("Caught invalid token: " + token, e);
            return Collections.emptyMap();
        }
    }
}
