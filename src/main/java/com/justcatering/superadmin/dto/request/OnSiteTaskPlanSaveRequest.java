package com.justcatering.superadmin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Screen 2 payload: save on-site manager task plan.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanSaveRequest {

    /** Optional related client UUID. */
    private UUID clientUuid;

    /** Manager selected on the on-site manager form. */
    @NotNull(message = "Manager is required")
    private UUID managerUuid;

    /** Optional additional notes (max 500 characters). */
    @Size(max = 500, message = "Additional notes must not exceed 500 characters")
    private String additionalNotes;

    /** Daily tasks filled on the form. */
    @NotEmpty(message = "At least one day task is required")
    private List<@Valid OnSiteTaskPlanDayRequest> days;
}
