package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a role.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    /** Human-readable role name. */
    @NotBlank(message = "Role name is required")
    @Size(max = 100, message = "Role name must not exceed 100 characters")
    private String name;

    /** Unique role code. */
    @NotBlank(message = "Role code is required")
    @Size(max = 50, message = "Role code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Role code must be uppercase letters, numbers, and underscores")
    private String code;

    /** Optional description. */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    /** Optional status (defaults to ACTIVE). */
    private EntityStatus status;

    /** Permission UUIDs to assign. */
    private Set<UUID> permissionUuids;
}
