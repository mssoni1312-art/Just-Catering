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
 * Manager assignment details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAssignmentDetailsResponse {

    private UUID uuid;
    private ClientDropdownResponse client;
    private UserDropdownResponse user;
    private DepartmentDropdownResponse department;
    private String projectName;
    private LocalDate closeDate;
    private BigDecimal rewardAmount;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
