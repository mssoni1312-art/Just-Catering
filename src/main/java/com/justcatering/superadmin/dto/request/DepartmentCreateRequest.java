package com.justcatering.superadmin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a department.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateRequest {

    /** Department name. */
    @NotBlank(message = "Department name is required")
    @Size(max = 150, message = "Department name must not exceed 150 characters")
    private String name;

    /** Optional unique code (auto-generated when omitted). */
    @Size(max = 50, message = "Department code must not exceed 50 characters")
    @Pattern(regexp = "^$|^[A-Z][A-Z0-9_]*$", message = "Department code must be uppercase letters, numbers, and underscores")
    private String code;

    /** Optional description. */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    /** Optional parent department UUID. */
    private UUID parentUuid;

    /** Members to add on create. */
    @Valid
    private List<DepartmentMemberRequest> members;
}
