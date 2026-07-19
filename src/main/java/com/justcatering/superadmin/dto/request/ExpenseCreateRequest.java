package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.ExpenseType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating an expense.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCreateRequest {

    /** Optional related client UUID. */
    private UUID clientUuid;

    /** Expense title. */
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    /** Expense category. */
    @NotNull(message = "Expense type is required")
    private ExpenseType expenseType;

    /** Claimant / team member UUID. */
    private UUID memberUserUuid;

    /** Date the expense was incurred. */
    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    /** Date the expense was paid. */
    private LocalDate paidDate;

    /** Payment due date. */
    private LocalDate dueDate;

    /** Expense amount. */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be zero or positive")
    private BigDecimal amount;

    /** Payment mode. */
    @Size(max = 50, message = "Payment mode must not exceed 50 characters")
    private String paymentMode;

    /** Travel origin city. */
    @Size(max = 100, message = "From city must not exceed 100 characters")
    private String fromCity;

    /** Travel destination city. */
    @Size(max = 100, message = "To city must not exceed 100 characters")
    private String toCity;

    /** Trip start date. */
    private LocalDate fromDate;

    /** Trip end date. */
    private LocalDate toDate;

    /** Distance travelled in kilometres. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Kilometres must be zero or positive")
    private BigDecimal km;

    /** Account or contact reference. */
    @Size(max = 200, message = "Account contact must not exceed 200 characters")
    private String accountContact;

    /** Optional remarks. */
    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    /** Optional bill / receipt URL. */
    @Size(max = 500, message = "Bill URL must not exceed 500 characters")
    private String billUrl;

    /** Optional entity status. */
    private EntityStatus status;
}
