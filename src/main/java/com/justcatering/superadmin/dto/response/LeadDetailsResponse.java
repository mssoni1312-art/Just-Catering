package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Meeting lead details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadDetailsResponse {

    private UUID uuid;
    private String ownerName;
    private String email;
    private String companyName;
    private String phone;
    private String address;
    private String state;
    private String city;
    private BigDecimal approxBudget;
    private ProductDropdownResponse product;
    private String notes;
    private LeadStage leadStage;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
