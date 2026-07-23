package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Aggregated expense totals for Overall Expenses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSummaryResponse {

    /** Dynamic category cards (preferred for Overall Expenses UI). */
    @Builder.Default
    private List<ExpenseCategoryResponse> categories = new ArrayList<>();

    /** Legacy fixed totals kept for older clients. */
    private BigDecimal travel;
    private BigDecimal food;
    private BigDecimal office;
    private BigDecimal other;
    private BigDecimal total;
}
