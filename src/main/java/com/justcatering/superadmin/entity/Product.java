package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Product catalog entity representing sellable Just Catering products.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    /** Product display name. */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** Unique product code. */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /** Product family/type (Figma Type field). */
    @Column(name = "product_type", nullable = false, length = 100)
    private String productType;

    /** Optional description. */
    @Column(name = "description", length = 500)
    private String description;

    /** Default reward amount for this product. */
    @Column(name = "default_reward_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal defaultRewardAmount = BigDecimal.ZERO;
}
