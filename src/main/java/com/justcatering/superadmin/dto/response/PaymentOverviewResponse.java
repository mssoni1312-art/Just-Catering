package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client payment overview with balance summary and recent receipts.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOverviewResponse {

    private UUID clientUuid;
    private String clientName;
    private BigDecimal productCost;
    private BigDecimal amountPaid;
    private BigDecimal remainingBalance;
    private List<PaymentListResponse> recentReceipts;
}
