package com.justcatering.superadmin.config;

import jakarta.persistence.AttributeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 * Stores {@link Instant} values as ISO-8601 text in SQLite.
 * <p>
 * Registered only for SQLite via {@link SqliteMetadataBuilderContributor}.
 * </p>
 */
public class SqliteInstantConverter implements AttributeConverter<Instant, String> {

    private static final DateTimeFormatter SQLITE_TIMESTAMP = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
            .optionalEnd()
            .toFormatter();

    @Override
    public String convertToDatabaseColumn(Instant instant) {
        return instant == null ? null : instant.toString();
    }

    @Override
    public Instant convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        if (isEpochMillis(dbData)) {
            return Instant.ofEpochMilli(Long.parseLong(dbData));
        }

        try {
            return Instant.parse(dbData);
        } catch (DateTimeParseException ignored) {
            LocalDateTime localDateTime = LocalDateTime.parse(dbData, SQLITE_TIMESTAMP);
            return localDateTime.toInstant(ZoneOffset.UTC);
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
