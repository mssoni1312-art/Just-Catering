package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Manager assignment list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAssignmentListResponse {

    private UUID uuid;
    private UUID clientUuid;
    private String clientName;
    private UUID userUuid;
    private String userName;
    private UUID departmentUuid;
    private String departmentName;
    private String projectName;
    private LocalDate closeDate;
    private BigDecimal rewardAmount;
    private EntityStatus status;
    private Instant createdAt;
}
