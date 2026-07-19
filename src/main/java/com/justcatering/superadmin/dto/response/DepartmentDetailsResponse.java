package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Department details response including members.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDetailsResponse {

    private UUID uuid;
    private String name;
    private String code;
    private String description;
    private EntityStatus status;
    private DepartmentDropdownResponse parent;
    private List<DepartmentMemberResponse> members;
    private Instant createdAt;
    private Instant updatedAt;
}
