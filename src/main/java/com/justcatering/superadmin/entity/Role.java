package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Role entity used for role-based access control.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    /** Human-readable role name. */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /** Unique role code used in security checks. */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /** Optional role description. */
    @Column(name = "description", length = 500)
    private String description;

    /** System roles cannot be deleted by end users. */
    @Column(name = "is_system", nullable = false)
    @Builder.Default
    private Boolean system = Boolean.FALSE;

    /** Role-permission associations. */
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<RolePermission> rolePermissions = new HashSet<>();

    /**
     * Returns active permissions granted to this role.
     *
     * @return set of active permissions
     */
    public Set<Permission> getActivePermissions() {
        return rolePermissions.stream()
                .filter(rp -> !rp.isDeleted())
                .map(RolePermission::getPermission)
                .filter(permission -> !permission.isDeleted())
                .collect(Collectors.toSet());
    }
}
