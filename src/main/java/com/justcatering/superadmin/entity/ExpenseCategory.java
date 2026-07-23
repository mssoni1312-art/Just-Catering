package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Expense category shown on the Overall Expenses screen.
 */
@Entity
@Table(name = "expense_categories")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCategory extends BaseEntity {

    /** Display name, e.g. Travel Expense. */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Optional legacy expense type code ({@code TRAVEL}, {@code FOOD}, {@code OFFICE}, {@code OTHER})
     * used to map amounts from existing expenses.
     */
    @Column(name = "code", length = 30)
    private String code;

    /** Optional icon key for the mobile UI. */
    @Column(name = "icon_key", length = 50)
    private String iconKey;

    /** Display order on Overall Expenses. */
    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;
}
