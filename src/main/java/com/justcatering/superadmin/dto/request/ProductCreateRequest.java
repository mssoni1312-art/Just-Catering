package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a product.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    /** Product name. */
    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;

    /** Optional unique code (auto-generated when omitted). */
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Pattern(regexp = "^$|^[A-Z][A-Z0-9_]*$", message = "Product code must be uppercase letters, numbers, and underscores")
    private String code;

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

    /** Optional status (defaults to ACTIVE). */
    private EntityStatus status;
}
