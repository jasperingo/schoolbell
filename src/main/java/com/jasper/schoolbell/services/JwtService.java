package com.jasper.schoolbell.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.User;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class JwtService {
    @Inject
    private Configuration configuration;

    private Instant getExpiration() {
        return Instant.now().plus(configuration.getJwtDuration(), ChronoUnit.DAYS);
    }

    public LocalDateTime getExpirationDate() {
        return LocalDateTime.ofInstant(getExpiration(), ZoneOffset.UTC);
    }

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withIssuer("auth0")
                .withExpiresAt(getExpiration())
                .sign(Algorithm.HMAC256(configuration.getJwtKey()));
    }

    public DecodedJWT verifyToken(final String token) {
        return JWT.require(Algorithm.HMAC256(configuration.getJwtKey()))
            .withIssuer("auth0")
            .build()
            .verify(token);
    }
}
