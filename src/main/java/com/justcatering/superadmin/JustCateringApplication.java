package com.justcatering.superadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Just Catering SuperAdmin application.
 * <p>
 * Bootstraps Spring Boot with JPA auditing, configuration properties scanning,
 * and scheduled tasks enabled.
 * </p>
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@ConfigurationPropertiesScan
@EnableScheduling
public class JustCateringApplication {

    /**
     * Starts the SuperAdmin application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(JustCateringApplication.class, args);
    }
}
