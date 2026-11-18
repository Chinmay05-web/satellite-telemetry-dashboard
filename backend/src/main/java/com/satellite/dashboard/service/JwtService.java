
package com.satellite.dashboard.service;

import com.satellite.dashboard.model.AppUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-minutes:60}")
    private long expirationMinutes;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(AppUser user) {

        Instant now = Instant.now();
        Instant expiry = now.plus(
                expirationMinutes, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(expiry)
                .claim("role", user.getRole())
                .claim("name", user.getName())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}
