package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for updating a product.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    /** Product name. */
    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;

    /** Product type / family. */
    @NotBlank(message = "Product type is required")
    @Size(max = 100, message = "Product type must not exceed 100 characters")
    private String productType;

    /** Optional description. */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    /** Default reward amount. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Default reward amount must be zero or positive")
    private BigDecimal defaultRewardAmount;

    /** Product status. */
    private EntityStatus status;
}
