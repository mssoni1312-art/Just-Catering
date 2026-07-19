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
 * Client deadline list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDeadlineListResponse {

    private UUID uuid;
    private UUID clientUuid;
    private String clientName;
    private UUID departmentUuid;
    private String departmentName;
    private LocalDate currentDeadline;
    private LocalDate newDeadline;
    private String reason;
    private EntityStatus status;
    private Instant createdAt;
}
