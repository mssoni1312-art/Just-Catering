package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.response.CalendarScheduleResponse;
import com.justcatering.superadmin.dto.response.CalendarSummaryResponse;
import java.time.LocalDate;

/**
 * Service for calendar summary and daily schedule APIs.
 */
public interface CalendarService {

    /**
     * Returns per-date status indicators for a calendar range.
     *
     * @param fromDate inclusive start date
     * @param toDate   inclusive end date
     * @return calendar summary
     */
    CalendarSummaryResponse getSummary(LocalDate fromDate, LocalDate toDate);

    /**
     * Returns clients scheduled on a specific date.
     *
     * @param date selected date
     * @return daily schedule
     */
    CalendarScheduleResponse getSchedule(LocalDate date);
}
