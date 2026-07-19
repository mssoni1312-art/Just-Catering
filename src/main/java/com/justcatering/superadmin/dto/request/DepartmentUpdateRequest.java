package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for updating a department.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUpdateRequest {

    /** Department name. */
    @NotBlank(message = "Department name is required")
    @Size(max = 150, message = "Department name must not exceed 150 characters")
    private String name;

    /** Optional description. */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    /** Optional parent department UUID (null clears parent). */
    private UUID parentUuid;

    /** Whether parent should be cleared when parentUuid is null. */
    private Boolean clearParent;

    /** Department status. */
    private EntityStatus status;

    /** Members to assign (replaces existing when provided). */
    @Valid
    private List<DepartmentMemberRequest> members;
}
