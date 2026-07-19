package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client deadline details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDeadlineDetailsResponse {

    private UUID uuid;
    private ClientDropdownResponse client;
    private DepartmentDropdownResponse department;
    private LocalDate currentDeadline;
    private LocalDate newDeadline;
    private String reason;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
