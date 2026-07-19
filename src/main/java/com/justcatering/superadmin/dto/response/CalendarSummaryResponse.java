package com.justcatering.superadmin.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Calendar month / range summary with per-date status dots.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarSummaryResponse {

    private LocalDate fromDate;
    private LocalDate toDate;
    private List<CalendarDateSummaryResponse> dates;
}
