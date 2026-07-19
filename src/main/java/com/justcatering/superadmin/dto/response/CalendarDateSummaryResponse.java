package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.CalendarEventStatus;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Status indicators for a single calendar date.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDateSummaryResponse {

    private LocalDate date;
    private Set<CalendarEventStatus> statuses;
}
