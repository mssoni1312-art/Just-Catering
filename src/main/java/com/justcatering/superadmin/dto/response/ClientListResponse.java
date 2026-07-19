package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
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
 * Client list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientListResponse {

    private UUID uuid;
    private String clientName;
    private String contactPerson;
    private String mobile;
    private String email;
    private String clientType;
    private String productName;
    private String productCode;
    private ClientStage clientStage;
    private Priority priority;
    private BigDecimal reward;
    private BigDecimal requirementsCompletionPercentage;
    private EntityStatus status;
    private LocalDate dealDate;
    private Instant createdAt;
}
