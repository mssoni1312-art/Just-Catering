package com.justcatering.superadmin.enums;

/**
 * Common lifecycle status used across domain entities.
 */
public enum EntityStatus {

    /** Entity is active and available for use. */
    ACTIVE,

    /** Entity is inactive and should not be used in normal flows. */
    INACTIVE,

    /** Entity is temporarily locked (typically users). */
    LOCKED,

    /** Entity is awaiting activation or approval. */
    PENDING,

    /** Entity has been revoked (typically tokens). */
    REVOKED
}
