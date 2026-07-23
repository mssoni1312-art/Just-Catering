package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.OnSiteTaskPlan;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link OnSiteTaskPlan} persistence.
 */
@Repository
public interface OnSiteTaskPlanRepository
        extends JpaRepository<OnSiteTaskPlan, Long>, JpaSpecificationExecutor<OnSiteTaskPlan> {

    /**
     * Finds a non-deleted task plan by UUID.
     *
     * @param uuid public UUID
     * @return optional task plan
     */
    Optional<OnSiteTaskPlan> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Loads a task plan with relations by UUID.
     *
     * @param uuid public UUID
     * @return optional task plan
     */
    @Query("""
            SELECT p FROM OnSiteTaskPlan p
            LEFT JOIN FETCH p.client c
            JOIN FETCH p.manager m
            WHERE p.uuid = :uuid
              AND p.deleted = FALSE
            """)
    Optional<OnSiteTaskPlan> findByUuidWithRelations(@Param("uuid") UUID uuid);
}
