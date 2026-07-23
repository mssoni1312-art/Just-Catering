package com.justcatering.superadmin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Day task payload for save.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanDayRequest {

    /** Day sequence number starting at 1. */
    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be at least 1")
    @Max(value = 365, message = "Day number must not exceed 365")
    private Integer dayNumber;

    /** Calendar date for this day. */
    @NotNull(message = "Task date is required")
    private LocalDate date;

    /** Required task description. */
    @NotBlank(message = "Task description is required")
    @Size(max = 300, message = "Task description must not exceed 300 characters")
    private String taskDescription;
}
