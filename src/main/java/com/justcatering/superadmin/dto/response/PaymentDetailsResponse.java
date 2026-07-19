package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.PaymentMode;
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
 * Payment details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsResponse {

    private UUID uuid;
    private String invoiceNumber;
    private ClientDropdownResponse client;
    private ProductDropdownResponse product;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String bankType;
    private PaymentMode paymentMode;
    private String remarks;
    private String receiptUrl;
    private BigDecimal productCost;
    private BigDecimal amountPaid;
    private BigDecimal remainingBalance;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
