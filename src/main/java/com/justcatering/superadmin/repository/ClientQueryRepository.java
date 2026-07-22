package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.ClientQuery;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.QueryStatus;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link ClientQuery} persistence operations.
 */
@Repository
public interface ClientQueryRepository
        extends JpaRepository<ClientQuery, Long>, JpaSpecificationExecutor<ClientQuery> {

    /**
     * Finds a non-deleted client query by UUID.
     *
     * @param uuid public UUID
     * @return optional client query
     */
    Optional<ClientQuery> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a client query with relations by UUID.
     *
     * @param uuid public UUID
     * @return optional client query
     */
    @Query("""
            SELECT q FROM ClientQuery q
            JOIN FETCH q.client c
            LEFT JOIN FETCH c.product p
            LEFT JOIN FETCH q.assignedUser u
            LEFT JOIN FETCH q.department d
            WHERE q.uuid = :uuid
              AND q.deleted = FALSE
            """)
    Optional<ClientQuery> findByUuidWithRelations(@Param("uuid") UUID uuid);

    /**
     * Finds active client queries for dropdowns.
     *
     * @param status entity status
     * @return client queries
     */
    List<ClientQuery> findByDeletedFalseAndStatusOrderByCreatedAtDescTitleAsc(EntityStatus status);

    /**
     * Counts non-deleted client queries.
     *
     * @return query count
     */
    long countByDeletedFalse();

    /**
     * Counts non-deleted client queries matching any of the given statuses.
     *
     * @param statuses query statuses
     * @return query count
     */
    long countByDeletedFalseAndQueryStatusIn(Collection<QueryStatus> statuses);

    /**
     * Counts non-deleted queries created within an instant range.
     *
     * @param fromInclusive inclusive start instant
     * @param toExclusive   exclusive end instant
     * @return query count
     */
    @Query("""
            SELECT COUNT(q)
            FROM ClientQuery q
            WHERE q.deleted = FALSE
              AND q.createdAt >= :fromInclusive
              AND q.createdAt < :toExclusive
            """)
    long countCreatedBetween(
            @Param("fromInclusive") Instant fromInclusive,
            @Param("toExclusive") Instant toExclusive
    );

    /**
     * Counts non-deleted queries created within a range and matching statuses.
     *
     * @param statuses      query statuses
     * @param fromInclusive inclusive start instant
     * @param toExclusive   exclusive end instant
     * @return query count
     */
    @Query("""
            SELECT COUNT(q)
            FROM ClientQuery q
            WHERE q.deleted = FALSE
              AND q.queryStatus IN :statuses
              AND q.createdAt >= :fromInclusive
              AND q.createdAt < :toExclusive
            """)
    long countCreatedBetweenAndQueryStatusIn(
            @Param("statuses") Collection<QueryStatus> statuses,
            @Param("fromInclusive") Instant fromInclusive,
            @Param("toExclusive") Instant toExclusive
    );
}
