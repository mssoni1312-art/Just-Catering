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
 * Full saved on-site manager task plan details.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanDetailsResponse {

    private UUID uuid;
    private ClientDropdownResponse client;
    private UserDropdownResponse manager;
    private String managerDesignation;
    private Integer numberOfDays;
    private String additionalNotes;
    private List<OnSiteTaskPlanDayResponse> days;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
