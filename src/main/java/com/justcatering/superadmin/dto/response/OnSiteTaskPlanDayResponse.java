package com.justcatering.superadmin.dto.response;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Day slot returned by prepare, or saved day in details.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanDayResponse {

    private UUID uuid;
    private Integer dayNumber;
    private LocalDate date;
    private String label;
    private String taskDescription;
}
