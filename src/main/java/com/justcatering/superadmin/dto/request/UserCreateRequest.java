package com.justcatering.superadmin.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a user.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    /** First name. */
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    /** Last name. */
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    /** Unique email address. */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /** Initial password. */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    /** Optional phone number. */
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = "^$|^[+0-9\\s-]{7,20}$", message = "Phone number format is invalid")
    private String phone;

    /** Optional company name. */
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    private String companyName;

    /** Optional initial status (defaults to ACTIVE). */
    private EntityStatus status;

    /**
     * Selected role code from the dropdown (e.g. {@code MEMBER}).
     * Preferred for single-role create forms.
     */
    @JsonAlias("role")
    @Size(max = 50, message = "Role code must not exceed 50 characters")
    private String roleCode;

    /** Role UUIDs, codes (e.g. MEMBER), or display names (e.g. Member) to assign. */
    private Set<String> roleUuids;

    /**
     * Validates that at least one role selector is provided.
     *
     * @return {@code true} when a role is present
     */
    @AssertTrue(message = "At least one role is required")
    @JsonIgnore
    public boolean isRoleSelectionValid() {
        boolean hasRoleCode = roleCode != null && !roleCode.isBlank();
        boolean hasRoleUuids = roleUuids != null
                && roleUuids.stream().anyMatch(value -> value != null && !value.isBlank());
        return hasRoleCode || hasRoleUuids;
    }
}
