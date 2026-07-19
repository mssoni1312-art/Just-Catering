package com.justcatering.superadmin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security configuration properties bound from {@code app.security.*}.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    /** Maximum consecutive failed login attempts before lock. */
    private int maxFailedLoginAttempts = 5;

    /** Account lock duration in minutes. */
    private int lockDurationMinutes = 30;
}
