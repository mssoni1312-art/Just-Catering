package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.DepartmentMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link DepartmentMember} persistence operations.
 */
@Repository
public interface DepartmentMemberRepository extends JpaRepository<DepartmentMember, Long> {

    /**
     * Finds all memberships for a department (including soft-deleted for reactivation).
     *
     * @param departmentId department id
     * @return memberships
     */
    List<DepartmentMember> findByDepartmentId(Long departmentId);

    /**
     * Finds active memberships for a department.
     *
     * @param departmentId department id
     * @return active memberships
     */
    List<DepartmentMember> findByDepartmentIdAndDeletedFalse(Long departmentId);

    /**
     * Finds an active membership by department and user UUID.
     *
     * @param departmentId department id
     * @param userUuid     user UUID
     * @return optional membership
     */
    @Query("""
            SELECT dm FROM DepartmentMember dm
            JOIN FETCH dm.user u
            WHERE dm.department.id = :departmentId
              AND u.uuid = :userUuid
              AND dm.deleted = FALSE
            """)
    Optional<DepartmentMember> findActiveByDepartmentIdAndUserUuid(
            @Param("departmentId") Long departmentId,
            @Param("userUuid") UUID userUuid
    );

    /**
     * Soft-deletes all active memberships for a department.
     *
     * @param departmentId department id
     * @return updated row count
     */
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE DepartmentMember dm
            SET dm.deleted = TRUE,
                dm.status = com.justcatering.superadmin.enums.EntityStatus.INACTIVE
            WHERE dm.department.id = :departmentId
              AND dm.deleted = FALSE
            """)
    int softDeleteByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Counts active memberships for a department.
     *
     * @param departmentId department id
     * @return member count
     */
    long countByDepartmentIdAndDeletedFalse(Long departmentId);

    /**
     * Finds active memberships for a user (for designation lookup).
     *
     * @param userId user id
     * @return active memberships
     */
    List<DepartmentMember> findByUserIdAndDeletedFalse(Long userId);
}
