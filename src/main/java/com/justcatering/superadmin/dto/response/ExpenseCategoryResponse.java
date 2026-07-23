package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Expense category card for Overall Expenses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCategoryResponse {

    private UUID uuid;
    private String name;
    private String code;
    private String iconKey;
    private Integer sortOrder;
    /** Aggregated amount for this category (populated on summary). */
    private BigDecimal amount;
}
