package com.justcatering.superadmin.enums;

/**
 * Client engagement / delivery stage.
 */
public enum ClientStage {

    /** Newly created client (e.g. converted from a lead). */
    NEW,

    /** Client is interested / early pipeline. */
    INTERESTED,

    /** Engagement is actively in progress. */
    IN_PROGRESS,

    /** Client is active and live. */
    ACTIVE,

    /** Engagement is temporarily on hold. */
    ON_HOLD,

    /** Engagement completed. */
    COMPLETED,

    /** Client has churned. */
    CHURNED
}
