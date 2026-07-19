package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Department / team entity used for organizational grouping of members.
 */
@Entity
@Table(name = "departments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {

    /** Department display name. */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** Unique department code. */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /** Optional description. */
    @Column(name = "description", length = 500)
    private String description;

    /** Optional parent department. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent;

    /** Member associations. */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<DepartmentMember> members = new HashSet<>();

    /**
     * Returns active (non-deleted) members.
     *
     * @return active members
     */
    public Set<DepartmentMember> getActiveMembers() {
        return members.stream()
                .filter(member -> !member.isDeleted())
                .collect(Collectors.toSet());
    }
}
