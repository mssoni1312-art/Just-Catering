package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Association entity linking a {@link User} to a {@link Department} with a designation.
 */
@Entity
@Table(name = "department_members")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentMember extends BaseEntity {

    /** Owning department. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /** Assigned user. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Job title within the department. */
    @Column(name = "designation", nullable = false, length = 150)
    private String designation;

    /** Whether this member leads the department. */
    @Column(name = "is_lead", nullable = false)
    @Builder.Default
    private Boolean lead = Boolean.FALSE;
}
