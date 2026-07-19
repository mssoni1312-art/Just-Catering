package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight payment option for dropdowns.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDropdownResponse {

    private UUID uuid;
    private String invoiceNumber;
    private String clientName;
    private LocalDate paymentDate;
    private BigDecimal amount;
}
