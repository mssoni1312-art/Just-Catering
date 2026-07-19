package com.justcatering.superadmin.enums;

/**
 * Sales pipeline stage for a meeting lead.
 */
public enum LeadStage {

    /** Newly captured lead. */
    NEW,

    /** Lead has been contacted. */
    CONTACTED,

    /** Lead is qualified for conversion. */
    QUALIFIED,

    /** Lead converted to client (stage only; no auto-client creation). */
    CONVERTED,

    /** Lead is lost / closed without conversion. */
    LOST
}
