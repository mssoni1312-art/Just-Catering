package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.ExpenseType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Expense list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseListResponse {

    private UUID uuid;
    private String title;
    private ExpenseType expenseType;
    private UUID clientUuid;
    private String clientName;
    private UUID memberUserUuid;
    private String memberUserName;
    private LocalDate expenseDate;
    private LocalDate paidDate;
    private LocalDate dueDate;
    private BigDecimal amount;
    private String paymentMode;
    private String fromCity;
    private String toCity;
    private EntityStatus status;
    private Instant createdAt;
}
