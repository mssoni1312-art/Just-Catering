package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private String profileImageUrl;
    private Boolean emailVerified;
    private EntityStatus status;
    private Instant lastLoginAt;
    private Instant passwordChangedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Set<RoleDropdownResponse> roles;
    private Set<String> permissions;
}
