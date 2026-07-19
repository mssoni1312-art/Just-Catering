package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.ClientManagerAssignment;
import java.math.BigDecimal;
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
 * Repository for {@link ClientManagerAssignment} persistence operations.
 */
@Repository
public interface ClientManagerAssignmentRepository
        extends JpaRepository<ClientManagerAssignment, Long>, JpaSpecificationExecutor<ClientManagerAssignment> {

    /**
     * Finds a non-deleted assignment by UUID.
     *
     * @param uuid public UUID
     * @return optional assignment
     */
    Optional<ClientManagerAssignment> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds an assignment with relations by UUID.
     *
     * @param uuid public UUID
     * @return optional assignment
     */
    @Query("""
            SELECT a FROM ClientManagerAssignment a
            JOIN FETCH a.client c
            LEFT JOIN FETCH a.department d
            JOIN FETCH a.user u
            WHERE a.uuid = :uuid
              AND a.deleted = FALSE
            """)
    Optional<ClientManagerAssignment> findByUuidWithRelations(@Param("uuid") UUID uuid);

    /**
     * Checks whether an active assignment exists for a client and user.
     *
     * @param clientId client id
     * @param userId   user id
     * @return {@code true} if exists
     */
    boolean existsByClientIdAndUserIdAndDeletedFalse(Long clientId, Long userId);

    /**
     * Checks assignment uniqueness excluding a record id.
     *
     * @param clientId  client id
     * @param userId    user id
     * @param excludeId assignment id to exclude
     * @return {@code true} if another active assignment exists
     */
    boolean existsByClientIdAndUserIdAndDeletedFalseAndIdNot(Long clientId, Long userId, Long excludeId);

    /**
     * Finds active assignments for a client with relations loaded.
     *
     * @param clientUuid client UUID
     * @return assignments
     */
    @Query("""
            SELECT a FROM ClientManagerAssignment a
            JOIN FETCH a.client c
            LEFT JOIN FETCH a.department d
            JOIN FETCH a.user u
            WHERE c.uuid = :clientUuid
              AND a.deleted = FALSE
            ORDER BY u.firstName ASC, u.lastName ASC
            """)
    List<ClientManagerAssignment> findActiveByClientUuid(@Param("clientUuid") UUID clientUuid);

    /**
     * Returns the project total reward stored on active assignments for a client.
     *
     * @param clientUuid client UUID
     * @return total reward amount
     */
    @Query("""
            SELECT MAX(a.rewardAmount) FROM ClientManagerAssignment a
            JOIN a.client c
            WHERE c.uuid = :clientUuid
              AND a.deleted = FALSE
              AND a.rewardAmount IS NOT NULL
            """)
    Optional<BigDecimal> maxRewardAmountByClientUuid(@Param("clientUuid") UUID clientUuid);

    /**
     * Returns project total rewards grouped by client UUID.
     *
     * @param clientUuids client UUIDs
     * @return pairs of client UUID and total reward amount
     */
    @Query("""
            SELECT c.uuid, MAX(a.rewardAmount) FROM ClientManagerAssignment a
            JOIN a.client c
            WHERE c.uuid IN :clientUuids
              AND a.deleted = FALSE
              AND a.rewardAmount IS NOT NULL
            GROUP BY c.uuid
            """)
    List<Object[]> maxRewardAmountsByClientUuids(@Param("clientUuids") Collection<UUID> clientUuids);

    /**
     * Counts active assignments for a client.
     *
     * @param clientUuid client UUID
     * @return active assignment count
     */
    @Query("""
            SELECT COUNT(a) FROM ClientManagerAssignment a
            JOIN a.client c
            WHERE c.uuid = :clientUuid
              AND a.deleted = FALSE
            """)
    long countActiveByClientUuid(@Param("clientUuid") UUID clientUuid);

    /**
     * Counts active assignments grouped by client UUID.
     *
     * @param clientUuids client UUIDs
     * @return pairs of client UUID and assignment count
     */
    @Query("""
            SELECT c.uuid, COUNT(a) FROM ClientManagerAssignment a
            JOIN a.client c
            WHERE c.uuid IN :clientUuids
              AND a.deleted = FALSE
            GROUP BY c.uuid
            """)
    List<Object[]> countActiveByClientUuids(@Param("clientUuids") Collection<UUID> clientUuids);
}
