package com.justcatering.superadmin.security.jwt;

import com.justcatering.superadmin.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility responsible for creating and validating JWT access tokens.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    /**
     * Generates a signed JWT access token.
     *
     * @param userId      user database id
     * @param userUuid    user public UUID
     * @param email       user email
     * @param roles       role codes
     * @param permissions permission codes
     * @return JWT string
     */
    public String generateAccessToken(
            Long userId,
            UUID userUuid,
            String email,
            List<String> roles,
            List<String> permissions
    ) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpirationMs());

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .claim("uid", userId)
                .claim("uuid", userUuid.toString())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates a JWT access token.
     *
     * @param token JWT string
     * @return {@code true} if valid
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.debug("JWT expired: {}", ex.getMessage());
        } catch (MalformedJwtException | UnsupportedJwtException | SecurityException | IllegalArgumentException ex) {
            log.warn("Invalid JWT: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Extracts the email (subject) from a JWT.
     *
     * @param token JWT string
     * @return email
     */
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the user id claim from a JWT.
     *
     * @param token JWT string
     * @return user id
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("uid", Long.class);
    }

    /**
     * Returns access token expiry in seconds.
     *
     * @return expiry seconds
     */
    public long getAccessTokenExpirySeconds() {
        return jwtProperties.getAccessTokenExpirationMs() / 1000;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            keyBytes = Decoders.BASE64.decode(
                    java.util.Base64.getEncoder().encodeToString(keyBytes)
            );
        }
        return Keys.hmacShaKeyFor(keyBytes.length >= 32
                ? keyBytes
                : java.util.Arrays.copyOf(keyBytes, 32));
    }
}
