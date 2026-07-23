package com.justcatering.superadmin.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.FollowUpReminder;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a follow-up.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpCreateRequest {

    /** Related client UUID (required for client follow-ups; do not send with leadUuid). */
    @JsonAlias({"clientId"})
    private UUID clientUuid;

    /** Related meeting lead UUID (required for lead follow-ups; do not send with clientUuid). */
    @JsonAlias({"leadId"})
    private UUID leadUuid;

    /** Follow-up title (Title Name). */
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @JsonAlias({"titleName"})
    private String title;

    /** Follow-up type (Call, Meeting, etc.). */
    private FollowUpType followUpType;

    /** Assigned team member UUID (Assign Member). */
    @JsonAlias({"assignMemberUuid", "assignedMemberUuid"})
    private UUID assignedUserUuid;

    /** Scheduled follow-up date. */
    @NotNull(message = "Follow-up date is required")
    private LocalDate followUpDate;

    /** Scheduled follow-up time. */
    private LocalTime followUpTime;

    /** Follow-up outcome status. */
    private FollowUpStatus followUpStatus;

    /** Expected budget / quotation amount. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Expected budget must be zero or positive")
    private BigDecimal expectedBudget;

    /** Remark / discussion notes (max 500 chars). */
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    @JsonAlias({"discussion", "discussionRemark"})
    private String remark;

    /** Schedule Next Follow-up toggle. */
    private Boolean scheduleNextFollowUp;

    /** Next follow-up date. */
    private LocalDate nextFollowUpDate;

    /** Next follow-up time. */
    private LocalTime nextFollowUpTime;

    /** Reminder preset (e.g. THIRTY_MINUTES = 30 Minutes Before). */
    private FollowUpReminder reminder;

    /** Reminder offset in minutes (alternative to {@link #reminder}). */
    @Min(value = 0, message = "Reminder minutes must be zero or positive")
    private Integer reminderMinutes;

    /** Optional entity status. */
    private EntityStatus status;
}
