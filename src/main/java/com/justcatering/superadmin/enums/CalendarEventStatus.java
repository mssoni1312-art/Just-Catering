package com.justcatering.superadmin.enums;

/**
 * Calendar dot / legend status shown on the calendar screen.
 */
public enum CalendarEventStatus {

    /** New client or early-stage engagement. */
    NEW,

    /** Completed client or follow-up. */
    COMPLETED,

    /** Engagement actively in progress. */
    IN_PROGRESS,

    /** Past-due follow-up that is still pending. */
    OVERDUE,

    /** Scheduled follow-up activity. */
    FOLLOW_UP
}
