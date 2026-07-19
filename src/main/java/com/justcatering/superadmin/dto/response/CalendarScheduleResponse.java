package com.justcatering.superadmin.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Daily schedule for a selected calendar date.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarScheduleResponse {

    private LocalDate date;
    private long totalCount;
    private List<CalendarScheduleItemResponse> items;
}
