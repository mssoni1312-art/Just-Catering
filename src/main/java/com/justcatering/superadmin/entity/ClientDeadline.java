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
 * Client project deadline change record.
 */
@Entity
@Table(name = "client_deadlines")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDeadline extends BaseEntity {

    /** Related client. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Optional responsible department. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /** Previous / current deadline before change. */
    @Column(name = "current_deadline", nullable = false)
    private LocalDate currentDeadline;

    /** Revised deadline after change. */
    @Column(name = "new_deadline", nullable = false)
    private LocalDate newDeadline;

    /** Reason for the deadline change. */
    @Column(name = "reason", nullable = false, length = 1000)
    private String reason;
}
