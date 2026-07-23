package com.justcatering.superadmin.config;

import jakarta.persistence.AttributeConverter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

/**
 * Stores {@link LocalDate} values as {@code yyyy-MM-dd} text in SQLite.
 * <p>
 * Also reads legacy epoch-millis values previously written by the dialect.
 * Registered only for SQLite via {@link SqliteMetadataBuilderContributor}.
 * </p>
 */
public class SqliteLocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        return date == null ? null : date.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        String value = dbData.trim();
        if (isEpochMillis(value)) {
            return Instant.ofEpochMilli(Long.parseLong(value))
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate();
        }

        try {
            return LocalDate.parse(value.length() >= 10 ? value.substring(0, 10) : value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Unable to parse LocalDate value: " + value, ex);
        }
    }

    private static boolean isEpochMillis(String value) {
        if (value.length() < 10 || value.length() > 13) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
