package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.LeadStage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Meeting lead entity representing a sales pipeline prospect.
 */
@Entity
@Table(name = "leads")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Lead extends BaseEntity {

    /** Lead first name. */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /** Lead last name. */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /** Email address. */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /** Company / organization name. */
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    /** Phone number. */
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    /** State. */
    @Column(name = "state", nullable = false, length = 100)
    private String state;

    /** City. */
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    /** Approximate budget. */
    @Column(name = "approx_budget", nullable = false, precision = 14, scale = 2)
    private BigDecimal approxBudget;

    /** Interested product (optional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /** Optional notes. */
    @Column(name = "notes", length = 1000)
    private String notes;

    /** Pipeline stage. */
    @Enumerated(EnumType.STRING)
    @Column(name = "lead_stage", nullable = false, length = 30)
    @Builder.Default
    private LeadStage leadStage = LeadStage.NEW;
}
