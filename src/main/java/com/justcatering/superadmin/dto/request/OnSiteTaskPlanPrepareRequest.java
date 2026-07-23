package com.justcatering.superadmin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Screen 1 payload: assign member and number of days for the next form.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanPrepareRequest {

    /** Member selected in "Assign To". */
    @NotNull(message = "Assigned member is required")
    private UUID assignedUserUuid;

    /** How many day slots to show on the next screen. */
    @NotNull(message = "Number of days is required")
    @Min(value = 1, message = "Number of days must be at least 1")
    @Max(value = 365, message = "Number of days must not exceed 365")
    private Integer numberOfDays;

    /** Optional start date used to prefill consecutive day dates. */
    private LocalDate startDate;
}
