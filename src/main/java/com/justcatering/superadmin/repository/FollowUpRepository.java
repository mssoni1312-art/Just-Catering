package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.FollowUp;
import com.justcatering.superadmin.enums.EntityStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link FollowUp} persistence operations.
 */
@Repository
public interface FollowUpRepository
        extends JpaRepository<FollowUp, Long>, JpaSpecificationExecutor<FollowUp> {

    /**
     * Finds a non-deleted follow-up by UUID.
     *
     * @param uuid public UUID
     * @return optional follow-up
     */
    Optional<FollowUp> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a follow-up with client and assigned user by UUID.
     *
     * @param uuid public UUID
     * @return optional follow-up
     */
    @Query("""
            SELECT f FROM FollowUp f
            LEFT JOIN FETCH f.client c
            LEFT JOIN FETCH f.lead l
            LEFT JOIN FETCH f.assignedUser u
            WHERE f.uuid = :uuid
              AND f.deleted = FALSE
            """)
    Optional<FollowUp> findByUuidWithRelations(@Param("uuid") UUID uuid);

    /**
     * Finds active follow-ups for dropdowns.
     *
     * @param status entity status
     * @return follow-ups
     */
    List<FollowUp> findByDeletedFalseAndStatusOrderByFollowUpDateDescTitleAsc(EntityStatus status);

    /**
     * Finds active follow-ups scheduled inside the given inclusive range.
     *
     * @param fromDate     start date
     * @param toDate       end date
     * @param entityStatus entity status
     * @return follow-ups
     */
    @Query("""
            SELECT f FROM FollowUp f
            LEFT JOIN FETCH f.client c
            LEFT JOIN FETCH f.lead l
            LEFT JOIN FETCH f.assignedUser u
            WHERE f.deleted = FALSE
              AND f.status = :entityStatus
              AND f.followUpDate >= :fromDate
              AND f.followUpDate <= :toDate
            ORDER BY f.followUpDate ASC, f.followUpTime ASC, f.title ASC
            """)
    List<FollowUp> findActiveFollowUpsBetween(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("entityStatus") EntityStatus entityStatus
    );

    /**
     * Finds active follow-ups scheduled on a specific date.
     *
     * @param date         follow-up date
     * @param entityStatus entity status
     * @return follow-ups
     */
    @Query("""
            SELECT f FROM FollowUp f
            LEFT JOIN FETCH f.client c
            LEFT JOIN FETCH f.lead l
            LEFT JOIN FETCH f.assignedUser u
            WHERE f.deleted = FALSE
              AND f.status = :entityStatus
              AND f.followUpDate = :date
            ORDER BY f.followUpTime ASC, f.title ASC
            """)
    List<FollowUp> findActiveFollowUpsByDate(
            @Param("date") LocalDate date,
            @Param("entityStatus") EntityStatus entityStatus
    );
}
