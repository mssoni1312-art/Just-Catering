package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.PaymentMode;
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
 * Request payload for creating a payment receipt.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Optional product UUID (defaults to client product). */
    private UUID productUuid;

    /** Invoice / receipt number. */
    @NotBlank(message = "Invoice number is required")
    @Size(max = 50, message = "Invoice number must not exceed 50 characters")
    private String invoiceNumber;

    /** Payment date. */
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    /** Paid amount. */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be zero or positive")
    private BigDecimal amount;

    /** Optional bank or channel label. */
    @Size(max = 50, message = "Bank type must not exceed 50 characters")
    private String bankType;

    /** Payment mode. */
    private PaymentMode paymentMode;

    /** Optional remarks. */
    private String remarks;

    /** Optional receipt document URL. */
    @Size(max = 500, message = "Receipt URL must not exceed 500 characters")
    private String receiptUrl;

    /** Optional entity status. */
    private EntityStatus status;
}
