package com.justcatering.superadmin.enums;

/**
 * Workflow status for client queries and requirements.
 */
public enum QueryStatus {

    /** Awaiting action. */
    PENDING,

    /** Work in progress. */
    IN_PROGRESS,

    /** Requires attention or escalation. */
    NEEDS_ATTENTION,

    /** Completed. */
    COMPLETED,

    /** Resolved / solved. */
    SOLVED
}
