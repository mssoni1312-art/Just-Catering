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
 * User list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private EntityStatus status;
    private Set<String> roles;
    private Instant lastLoginAt;
    private Instant createdAt;
}
