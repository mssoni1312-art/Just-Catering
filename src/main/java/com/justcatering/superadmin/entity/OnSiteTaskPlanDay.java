package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Single day task entry within an on-site manager task plan.
 */
@Entity
@Table(name = "onsite_task_plan_days")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlanDay extends BaseEntity {

    /** Parent task plan. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_plan_id", nullable = false)
    private OnSiteTaskPlan taskPlan;

    /** Day sequence number starting at 1. */
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    /** Calendar date for this day. */
    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    /** Required task description (max 300 characters). */
    @Column(name = "task_description", nullable = false, length = 300)
    private String taskDescription;
}
