package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for recording a client deadline change.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDeadlineCreateRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Optional department UUID. */
    private UUID departmentUuid;

    /** Previous / current deadline. */
    @NotNull(message = "Current deadline is required")
    private LocalDate currentDeadline;

    /** Revised deadline. */
    @NotNull(message = "New deadline is required")
    private LocalDate newDeadline;

    /** Reason for the change. */
    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    private String reason;

    /** Optional entity status. */
    private EntityStatus status;
}
