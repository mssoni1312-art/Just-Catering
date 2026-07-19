package com.justcatering.superadmin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Preset reminder options shown in the follow-up form.
 */
@Getter
@RequiredArgsConstructor
public enum FollowUpReminder {

    /** Remind 5 minutes before. */
    FIVE_MINUTES(5, "5 Minutes Before"),

    /** Remind 15 minutes before. */
    FIFTEEN_MINUTES(15, "15 Minutes Before"),

    /** Remind 30 minutes before. */
    THIRTY_MINUTES(30, "30 Minutes Before"),

    /** Remind 1 hour before. */
    ONE_HOUR(60, "1 Hour Before"),

    /** Remind 1 day before. */
    ONE_DAY(1440, "1 Day Before");

    private final int minutes;
    private final String label;
}
