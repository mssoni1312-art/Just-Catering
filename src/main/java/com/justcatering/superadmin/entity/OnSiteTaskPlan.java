package com.justcatering.superadmin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * On-site manager task plan with daily tasks.
 */
@Entity
@Table(name = "onsite_task_plans")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OnSiteTaskPlan extends BaseEntity {

    /** Optional related client. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    /** Manager / member the plan is assigned to. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    /** Optional additional instructions. */
    @Column(name = "additional_notes", length = 500)
    private String additionalNotes;

    /** Total number of planned days. */
    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    /** Daily task entries. */
    @OneToMany(mappedBy = "taskPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    @Builder.Default
    private List<OnSiteTaskPlanDay> days = new ArrayList<>();
}
