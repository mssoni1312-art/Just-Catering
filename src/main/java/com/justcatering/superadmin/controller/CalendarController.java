package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.CalendarScheduleResponse;
import com.justcatering.superadmin.dto.response.CalendarSummaryResponse;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for calendar summary and daily schedule APIs.
 */
@RestController
@RequestMapping(AppConstants.CALENDAR_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Calendar", description = "Calendar summary dots and daily client schedule APIs")
public class CalendarController {

    private final CalendarService calendarService;

    /**
     * Returns calendar status dots for a month or custom date range.
     *
     * <p>Provide either {@code year} + {@code month} for month view, or {@code fromDate} + {@code toDate}
     * for week/custom ranges.</p>
     *
     * @return per-date status indicators
     */
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('CALENDAR_VIEW')")
    @Operation(summary = "Get calendar summary for a month or date range")
    public ResponseEntity<ApiResponse<CalendarSummaryResponse>> getSummary(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        LocalDate rangeStart;
        LocalDate rangeEnd;

        if (year != null || month != null) {
            if (year == null || month == null) {
                throw new BusinessException("Both year and month are required for month view");
            }
            YearMonth yearMonth = YearMonth.of(year, month);
            rangeStart = yearMonth.atDay(1);
            rangeEnd = yearMonth.atEndOfMonth();
        } else if (fromDate != null && toDate != null) {
            rangeStart = fromDate;
            rangeEnd = toDate;
        } else {
            throw new BusinessException(
                    "Provide either year and month, or fromDate and toDate"
            );
        }

        CalendarSummaryResponse data = calendarService.getSummary(rangeStart, rangeEnd);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns clients scheduled on the selected date.
     *
     * @param date selected calendar date
     * @return daily schedule list
     */
    @GetMapping("/schedule")
    @PreAuthorize("hasAuthority('CALENDAR_VIEW')")
    @Operation(summary = "Get daily client schedule for a date")
    public ResponseEntity<ApiResponse<CalendarScheduleResponse>> getSchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        CalendarScheduleResponse data = calendarService.getSchedule(date);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
