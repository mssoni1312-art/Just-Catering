package com.justcatering.superadmin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Member assignment payload for department create/update.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentMemberRequest {

    /** User UUID to assign. */
    @NotNull(message = "User UUID is required")
    private UUID userUuid;

    /** Job designation within the department. */
    @NotBlank(message = "Designation is required")
    @Size(max = 150, message = "Designation must not exceed 150 characters")
    private String designation;

    /** Whether this member is the department lead. */
    private Boolean lead;
}
