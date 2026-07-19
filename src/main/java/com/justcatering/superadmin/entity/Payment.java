package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.PaymentMode;
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
 * Payment entity representing a client payment receipt / invoice.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    /** Related client. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Related product (defaults from client when omitted). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /** Unique invoice / receipt number. */
    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    /** Payment date. */
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    /** Paid amount. */
    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    /** Optional bank or channel label (e.g. GPay, NEFT). */
    @Column(name = "bank_type", length = 50)
    private String bankType;

    /** Payment mode. */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false, length = 30)
    @Builder.Default
    private PaymentMode paymentMode = PaymentMode.CASH;

    /** Optional remarks. */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    /** Optional receipt document URL. */
    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;
}
