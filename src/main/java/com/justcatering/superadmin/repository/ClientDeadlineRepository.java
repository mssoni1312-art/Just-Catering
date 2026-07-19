package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.ClientDeadline;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link ClientDeadline} persistence operations.
 */
@Repository
public interface ClientDeadlineRepository
        extends JpaRepository<ClientDeadline, Long>, JpaSpecificationExecutor<ClientDeadline> {

    /**
     * Finds a non-deleted deadline record by UUID.
     *
     * @param uuid public UUID
     * @return optional deadline record
     */
    Optional<ClientDeadline> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a deadline record with relations by UUID.
     *
     * @param uuid public UUID
     * @return optional deadline record
     */
    @Query("""
            SELECT d FROM ClientDeadline d
            JOIN FETCH d.client c
            LEFT JOIN FETCH d.department dept
            WHERE d.uuid = :uuid
              AND d.deleted = FALSE
            """)
    Optional<ClientDeadline> findByUuidWithRelations(@Param("uuid") UUID uuid);
}
