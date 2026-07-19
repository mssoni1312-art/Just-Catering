package com.justcatering.superadmin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CORS configuration properties bound from {@code app.cors.*}.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /** Comma-separated allowed origins. */
    private String allowedOrigins = "http://localhost:3000";
}
