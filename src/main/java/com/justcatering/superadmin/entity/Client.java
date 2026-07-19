package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.Priority;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Client entity representing a CRM customer / company.
 */
@Entity
@Table(name = "clients")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    /** Client / company name. */
    @Column(name = "client_name", nullable = false, length = 200)
    private String clientName;

    /** Primary contact person. */
    @Column(name = "contact_person", nullable = false, length = 150)
    private String contactPerson;

    /** Mobile number. */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /** Unique email address. */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /** Optional GST number. */
    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    /** Business type (e.g. Catering Services). */
    @Column(name = "client_type", nullable = false, length = 100)
    private String clientType;

    /** Purchased / interested product. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Deal / engagement date. */
    @Column(name = "deal_date")
    private LocalDate dealDate;

    /** Total deal amount. */
    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /** Optional budget. */
    @Column(name = "budget", precision = 14, scale = 2)
    private BigDecimal budget;

    /** Optional notes. */
    @Column(name = "notes", length = 300)
    private String notes;

    /** Engagement stage. */
    @Enumerated(EnumType.STRING)
    @Column(name = "client_stage", nullable = false, length = 30)
    @Builder.Default
    private ClientStage clientStage = ClientStage.ACTIVE;

    /** Priority. */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    /** Requirements completion percentage (0-100). */
    @Column(name = "requirements_completion_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal requirementsCompletionPercentage = BigDecimal.ZERO;

    /** Optional state. */
    @Column(name = "state", length = 100)
    private String state;

    /** Optional city. */
    @Column(name = "city", length = 100)
    private String city;
}
