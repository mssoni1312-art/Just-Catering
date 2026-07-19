package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Manager / team member assignment for a client project.
 */
@Entity
@Table(name = "client_manager_assignments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientManagerAssignment extends BaseEntity {

    /** Related client. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Optional responsible department. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /** Assigned manager or team member. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Optional project label. */
    @Column(name = "project_name", length = 200)
    private String projectName;

    /** Target project close date. */
    @Column(name = "close_date")
    private LocalDate closeDate;

    /** Optional reward or incentive amount. */
    @Column(name = "reward_amount", precision = 14, scale = 2)
    private BigDecimal rewardAmount;
}
