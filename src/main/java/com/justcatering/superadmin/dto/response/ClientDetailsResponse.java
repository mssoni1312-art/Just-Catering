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
 * Client details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDetailsResponse {

    private UUID uuid;
    private String clientName;
    private String contactPerson;
    private String mobile;
    private String email;
    private String gstNumber;
    private String clientType;
    private ProductDropdownResponse product;
    private LocalDate dealDate;
    private BigDecimal reward;
    private BigDecimal totalAmount;
    private BigDecimal budget;
    private String notes;
    private ClientStage clientStage;
    private Priority priority;
    private BigDecimal requirementsCompletionPercentage;
    private String state;
    private String city;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
