package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for updating a manager assignment.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAssignmentUpdateRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Assigned manager / team member UUID. */
    @NotNull(message = "User is required")
    private UUID userUuid;

    /** Optional department UUID. */
    private UUID departmentUuid;

    /** Optional project label. */
    @Size(max = 200, message = "Project name must not exceed 200 characters")
    private String projectName;

    /** Target project close date. */
    private LocalDate closeDate;

    /** Optional reward amount. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Reward amount must be zero or positive")
    private BigDecimal rewardAmount;

    /** Optional entity status. */
    private EntityStatus status;
}
