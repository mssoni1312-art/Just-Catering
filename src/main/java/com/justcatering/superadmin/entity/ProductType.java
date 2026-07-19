package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Product type / family lookup used in the Add Product modal.
 */
@Entity
@Table(name = "product_types")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductType extends BaseEntity {

    /** Display name shown in the Type dropdown. */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
