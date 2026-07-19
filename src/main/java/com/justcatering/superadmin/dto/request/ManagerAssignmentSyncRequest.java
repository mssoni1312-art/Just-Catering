package com.justcatering.superadmin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for syncing manager assignments from the assignment screen.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAssignmentSyncRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Project label shared across assigned members. */
    @NotBlank(message = "Project name is required")
    @Size(max = 200, message = "Project name must not exceed 200 characters")
    private String projectName;

    /** Target project close date. */
    @NotNull(message = "Close date is required")
    private LocalDate closeDate;

    /** Total reward amount to split equally across assigned members. */
    @NotNull(message = "Reward amount is required")
    @DecimalMin(value = "0.01", message = "Reward amount must be greater than zero")
    private BigDecimal rewardAmount;

    /** Selected department members for the client project. */
    @NotEmpty(message = "At least one member is required")
    private List<@Valid ManagerAssignmentMemberRequest> members;
}
