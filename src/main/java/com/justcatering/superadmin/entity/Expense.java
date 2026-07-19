package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.ExpenseType;
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
 * Expense entity representing travel, food, office, and other business costs.
 */
@Entity
@Table(name = "expenses")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends BaseEntity {

    /** Optional related client. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    /** Expense title. */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** Expense category. */
    @Enumerated(EnumType.STRING)
    @Column(name = "expense_type", nullable = false, length = 30)
    private ExpenseType expenseType;

    /** Claimant / team member. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_user_id")
    private User memberUser;

    /** Date the expense was incurred. */
    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    /** Date the expense was paid. */
    @Column(name = "paid_date")
    private LocalDate paidDate;

    /** Payment due date. */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /** Expense amount. */
    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    /** Payment mode (e.g. UPI, Cash, Card). */
    @Column(name = "payment_mode", length = 50)
    private String paymentMode;

    /** Travel origin city. */
    @Column(name = "from_city", length = 100)
    private String fromCity;

    /** Travel destination city. */
    @Column(name = "to_city", length = 100)
    private String toCity;

    /** Trip start date. */
    @Column(name = "from_date")
    private LocalDate fromDate;

    /** Trip end date. */
    @Column(name = "to_date")
    private LocalDate toDate;

    /** Distance travelled in kilometres. */
    @Column(name = "km", precision = 10, scale = 2)
    private BigDecimal km;

    /** Account or contact reference. */
    @Column(name = "account_contact", length = 200)
    private String accountContact;

    /** Optional remarks. */
    @Column(name = "remarks", length = 1000)
    private String remarks;

    /** Optional bill / receipt URL. */
    @Column(name = "bill_url", length = 500)
    private String billUrl;
}
