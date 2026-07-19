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
 * Role details response including permissions.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDetailsResponse {

    private UUID uuid;
    private String name;
    private String code;
    private String description;
    private Boolean system;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Set<PermissionListResponse> permissions;
}
