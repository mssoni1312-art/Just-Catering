package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base mapped superclass providing common audit and soft-delete columns
 * for every domain entity.
 * <p>
 * All tables must include: id, uuid, created_at, updated_at, created_by,
 * updated_by, deleted, status, and version.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /** Surrogate primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /** Public-facing unique identifier. */
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    /** Record creation timestamp (UTC). */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Record last update timestamp (UTC). */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** User id who created the record. */
    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    /** User id who last updated the record. */
    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    /** Soft-delete flag. */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    /** Lifecycle status. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private EntityStatus status;

    /** Optimistic locking version. */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * Initializes default values before first persist.
     */
    @PrePersist
    protected void onCreate() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (deleted == null) {
            deleted = Boolean.FALSE;
        }
        if (status == null) {
            status = EntityStatus.ACTIVE;
        }
        if (version == null) {
            version = 0L;
        }
    }

    /**
     * Hook reserved for future update-time defaults.
     */
    @PreUpdate
    protected void onUpdate() {
        // Intentionally left for extension
    }

    /**
     * Marks the entity as soft-deleted.
     */
    public void softDelete() {
        this.deleted = Boolean.TRUE;
        this.status = EntityStatus.INACTIVE;
    }

    /**
     * Returns whether the entity is soft-deleted.
     *
     * @return {@code true} if deleted
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(deleted);
    }
}
