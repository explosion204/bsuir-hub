package com.karnyshov.bsuirhub.util.impl;

import com.karnyshov.bsuirhub.util.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.inject.Named;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

@Named
public class JwtServiceImpl implements JwtService {
    private static final Logger logger = LogManager.getLogger();
    private static final String JWT_PROPERTIES_NAME = "jwt.properties";
    private static final String SECRET_KEY_PROPERTY = "secretKey";
    private static final String VALIDITY_TIME_PROPERTY = "validityTime";

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
            throw new RuntimeException("Unable to read jwt properties", e);
        }
    }

    @Override
    public String generateJwt(long userId, String email) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, validityTime);
        Date expirationTime = calendar.getTime();

        return Jwts.builder()
                .claim(ID_CLAIM, userId)
                .claim(EMAIL_CLAIM, email)
                .setExpiration(expirationTime)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Optional<Pair<Long, String>> parseJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            long userId = ((Double) claims.get(ID_CLAIM)).longValue();
            String email = (String) claims.get(EMAIL_CLAIM);
            Pair<Long, String> pair = Pair.of(userId, email);

            return Optional.of(pair);
        } catch (JwtException | NumberFormatException e) {
            logger.error("Caught invalid token: " + token, e);
            return Optional.empty();
        }
    }
}
