package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.RolePermission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link RolePermission} persistence operations.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * Finds active or soft-deleted links for a role (including deleted for reactivation).
     *
     * @param roleId role id
     * @return associations
     */
    List<RolePermission> findByRoleId(Long roleId);

    /**
     * Finds active role-permission links for a role.
     *
     * @param roleId role id
     * @return associations
     */
    List<RolePermission> findByRoleIdAndDeletedFalse(Long roleId);

    /**
     * Soft-deletes all active permission links for a role.
     *
     * @param roleId role id
     * @return updated row count
     */
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE RolePermission rp
            SET rp.deleted = TRUE,
                rp.status = com.justcatering.superadmin.enums.EntityStatus.INACTIVE
            WHERE rp.role.id = :roleId
              AND rp.deleted = FALSE
            """)
    int softDeleteByRoleId(@Param("roleId") Long roleId);
}
