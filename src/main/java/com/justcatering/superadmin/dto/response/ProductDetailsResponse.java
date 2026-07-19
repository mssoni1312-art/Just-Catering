package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponse {

    private UUID uuid;
    private String name;
    private String code;
    private String productType;
    private String description;
    private BigDecimal defaultRewardAmount;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
