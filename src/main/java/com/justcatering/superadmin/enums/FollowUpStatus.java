package com.justcatering.superadmin.enums;

/**
 * Outcome status of a client follow-up.
 */
public enum FollowUpStatus {

    /** Scheduled and not yet completed. */
    PENDING,

    /** Successfully completed. */
    COMPLETED,

    /** Cancelled before completion. */
    CANCELLED,

    /** Attempted but no response received. */
    NO_RESPONSE
}
