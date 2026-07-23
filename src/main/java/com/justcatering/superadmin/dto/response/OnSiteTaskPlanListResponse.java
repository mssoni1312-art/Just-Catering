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
 * Task plan card for the saved plans list screen (manager-wise).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanListResponse {

    private UUID uuid;

    /** Display date on the card (first day of the plan). */
    private LocalDate planDate;

    private LocalDate startDate;
    private LocalDate endDate;

    private UUID clientUuid;
    private String clientName;

    private UUID managerUuid;
    private String managerName;
    private String managerEmail;
    /** Avatar initials, e.g. {@code RS}. */
    private String managerInitials;
    /** Job title under the name, e.g. {@code Sales Manager}. */
    private String managerDesignation;

    private Integer numberOfDays;
    /** Badge text, e.g. {@code 4-Day Task Plan}. */
    private String planLabel;

    private EntityStatus status;
    private Instant createdAt;
}
