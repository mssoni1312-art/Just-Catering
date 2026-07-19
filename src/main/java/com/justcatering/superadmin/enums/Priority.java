package com.justcatering.superadmin.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Priority levels used across clients, queries, and follow-ups.
 */
public enum Priority {

    /** Low priority. */
    LOW,

    /** Medium priority. */
    MEDIUM,

    /** High priority. */
    HIGH,

    /** Urgent priority. */
    URGENT;

    /**
     * Parses priority from UI or API values (case-insensitive).
     *
     * @param value raw priority string
     * @return matching priority
     */
    @JsonCreator
    public static Priority fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Priority.valueOf(value.trim().toUpperCase());
    }
}
