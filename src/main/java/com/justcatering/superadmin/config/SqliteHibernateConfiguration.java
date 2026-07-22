package com.justcatering.superadmin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Applies SQLite-only Hibernate settings when the SQLite datasource is active.
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "org.sqlite.JDBC")
public class SqliteHibernateConfiguration {

    /**
     * Registers SQLite-specific attribute converters for Hibernate.
     *
     * @return Hibernate properties customizer
     */
    @Bean
    public HibernatePropertiesCustomizer sqliteHibernatePropertiesCustomizer() {
        return hibernateProperties -> hibernateProperties.put(
                "hibernate.metadata_builder_contributor",
                SqliteMetadataBuilderContributor.class.getName()
        );
    }
}
