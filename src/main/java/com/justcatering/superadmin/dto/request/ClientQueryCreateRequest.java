package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a client query.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientQueryCreateRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Query title. */
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    /** Optional query type. */
    @Size(max = 100, message = "Type must not exceed 100 characters")
    private String queryType;

    /** Assigned team member UUID. */
    private UUID assignedUserUuid;

    /** Department UUID. */
    private UUID departmentUuid;

    /** Priority level. */
    private Priority priority;

    /** Workflow status. */
    private QueryStatus queryStatus;

    /** Optional remarks. */
    private String remarks;

    /** Optional image path. */
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    /** Completion timestamp. */
    private Instant completedAt;

    /** Optional entity status. */
    private EntityStatus status;
}
