package com.justcatering.superadmin.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A team member included in a manager assignment sync request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAssignmentMemberRequest {

    /** Assigned team member UUID. */
    @NotNull(message = "Member user is required")
    private UUID userUuid;

    /** Optional department UUID for the member. */
    private UUID departmentUuid;
}
