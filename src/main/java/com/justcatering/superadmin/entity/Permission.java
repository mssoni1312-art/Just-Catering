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
 * Permission entity representing a fine-grained access right.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {

    /** Human-readable permission name. */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** Unique permission code used in authorization checks. */
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    /** Functional module this permission belongs to. */
    @Column(name = "module", nullable = false, length = 100)
    private String module;

    /** Optional description. */
    @Column(name = "description", length = 500)
    private String description;
}
