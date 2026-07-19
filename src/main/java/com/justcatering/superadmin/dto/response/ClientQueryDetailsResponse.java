package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client query details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientQueryDetailsResponse {

    private UUID uuid;
    private String title;
    private String queryType;
    private ClientDropdownResponse client;
    private UserDropdownResponse assignedUser;
    private DepartmentDropdownResponse department;
    private Priority priority;
    private QueryStatus queryStatus;
    private String remarks;
    private String imageUrl;
    private Instant completedAt;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
