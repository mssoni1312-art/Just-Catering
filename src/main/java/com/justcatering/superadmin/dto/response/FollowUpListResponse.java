package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Follow-up list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpListResponse {

    private UUID uuid;
    private String title;
    private FollowUpType followUpType;
    private UUID clientUuid;
    private String clientName;
    private UUID leadUuid;
    private String leadName;
    private UUID assignedUserUuid;
    private String assignedUserName;
    private LocalDate followUpDate;
    private LocalTime followUpTime;
    private FollowUpStatus followUpStatus;
    private BigDecimal expectedBudget;
    private EntityStatus status;
    private Instant createdAt;
}
