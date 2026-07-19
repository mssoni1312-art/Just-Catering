package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link UserRole} persistence operations.
 */
@Repository
public interface UserRoleEntityRepository extends JpaRepository<UserRole, Long> {

    /**
     * Finds all user-role links for a user (including soft-deleted for reactivation).
     *
     * @param userId user id
     * @return associations
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * Finds active user-role links for a user.
     *
     * @param userId user id
     * @return associations
     */
    List<UserRole> findByUserIdAndDeletedFalse(Long userId);

    /**
     * Soft-deletes all active role links for a user.
     *
     * @param userId user id
     * @return updated row count
     */
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE UserRole ur
            SET ur.deleted = TRUE,
                ur.status = com.justcatering.superadmin.enums.EntityStatus.INACTIVE
            WHERE ur.user.id = :userId
              AND ur.deleted = FALSE
            """)
    int softDeleteByUserId(@Param("userId") Long userId);

    /**
     * Counts non-deleted users assigned to a role.
     *
     * @param roleId role id
     * @return assignment count
     */
    long countByRoleIdAndDeletedFalse(Long roleId);
}
