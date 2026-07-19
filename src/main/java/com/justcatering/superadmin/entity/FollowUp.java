package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Follow-up entity representing a scheduled client engagement activity.
 */
@Entity
@Table(name = "follow_ups")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUp extends BaseEntity {

    /** Related client (optional when {@link #leadUuid} is provided). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    /** Related meeting lead before client conversion. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    /** Follow-up title / subject. */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** Activity type. */
    @Enumerated(EnumType.STRING)
    @Column(name = "follow_up_type", nullable = false, length = 30)
    @Builder.Default
    private FollowUpType followUpType = FollowUpType.CALL;

    /** Assigned team member. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    /** Scheduled follow-up date. */
    @Column(name = "follow_up_date", nullable = false)
    private LocalDate followUpDate;

    /** Scheduled follow-up time. */
    @Column(name = "follow_up_time")
    private LocalTime followUpTime;

    /** Follow-up outcome status. */
    @Enumerated(EnumType.STRING)
    @Column(name = "follow_up_status", nullable = false, length = 30)
    @Builder.Default
    private FollowUpStatus followUpStatus = FollowUpStatus.PENDING;

    /** Expected deal budget discussed. */
    @Column(name = "expected_budget", precision = 14, scale = 2)
    private BigDecimal expectedBudget;

    /** Optional remarks. */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /** Next scheduled follow-up date. */
    @Column(name = "next_follow_up_date")
    private LocalDate nextFollowUpDate;

    /** Next scheduled follow-up time. */
    @Column(name = "next_follow_up_time")
    private LocalTime nextFollowUpTime;

    /** Reminder offset in minutes before follow-up. */
    @Column(name = "reminder_minutes")
    private Integer reminderMinutes;
}
