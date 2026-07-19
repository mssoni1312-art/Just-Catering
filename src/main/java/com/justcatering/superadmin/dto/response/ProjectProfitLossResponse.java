package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Computed project profit and loss summary for a client.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProfitLossResponse {

    private UUID clientUuid;
    private String clientName;
    private BigDecimal income;
    private BigDecimal totalExpenses;
    private BigDecimal profit;
    private BigDecimal profitMarginPercent;
    private BigDecimal expenseRatioPercent;
}
