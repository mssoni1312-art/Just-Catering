package com.justcatering.superadmin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT configuration properties bound from {@code app.jwt.*}.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /** HMAC signing secret. */
    private String secret;

    /** Access token lifetime in milliseconds. */
    private long accessTokenExpirationMs;

    /** Refresh token lifetime in milliseconds. */
    private long refreshTokenExpirationMs;
}
