package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Requirement;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Requirement} persistence operations.
 */
@Repository
public interface RequirementRepository
        extends JpaRepository<Requirement, Long>, JpaSpecificationExecutor<Requirement> {

    /**
     * Finds a non-deleted requirement by UUID.
     *
     * @param uuid public UUID
     * @return optional requirement
     */
    Optional<Requirement> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a requirement with relations by UUID.
     *
     * @param uuid public UUID
     * @return optional requirement
     */
    @Query("""
            SELECT r FROM Requirement r
            JOIN FETCH r.client c
            LEFT JOIN FETCH r.assignedUser u
            WHERE r.uuid = :uuid
              AND r.deleted = FALSE
            """)
    Optional<Requirement> findByUuidWithRelations(@Param("uuid") UUID uuid);

    /**
     * Finds active requirements for dropdowns.
     *
     * @param status entity status
     * @return requirements
     */
    List<Requirement> findByDeletedFalseAndStatusOrderByCheckInAtDescTitleAsc(EntityStatus status);

    /**
     * Finds active requirements for legacy UUID resolution.
     *
     * @return active requirements
     */
    List<Requirement> findByDeletedFalse();
}
