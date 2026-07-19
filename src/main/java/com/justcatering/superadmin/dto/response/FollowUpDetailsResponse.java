package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.FollowUpReminder;
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
 * Follow-up details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpDetailsResponse {

    private UUID uuid;
    private String title;
    private FollowUpType followUpType;
    private ClientDropdownResponse client;
    private LeadDropdownResponse lead;
    private UserDropdownResponse assignedUser;
    private LocalDate followUpDate;
    private LocalTime followUpTime;
    private FollowUpStatus followUpStatus;
    private BigDecimal expectedBudget;
    private String remark;
    private Boolean scheduleNextFollowUp;
    private LocalDate nextFollowUpDate;
    private LocalTime nextFollowUpTime;
    private Integer reminderMinutes;
    private FollowUpReminder reminder;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
