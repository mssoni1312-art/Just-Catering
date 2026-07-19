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
 * Payment list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListResponse {

    private UUID uuid;
    private String invoiceNumber;
    private UUID clientUuid;
    private String clientName;
    private UUID productUuid;
    private String productName;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String bankType;
    private PaymentMode paymentMode;
    private EntityStatus status;
    private Instant createdAt;
}
