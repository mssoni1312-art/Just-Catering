package com.justcatering.superadmin.config;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;

/**
 * Registers SQLite-specific JPA converters without affecting PostgreSQL deployments.
 */
public class SqliteMetadataBuilderContributor implements MetadataBuilderContributor {

    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applyAttributeConverter(new SqliteInstantConverter(), true);
    }
}
