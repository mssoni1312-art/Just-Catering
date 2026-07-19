package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Permission} persistence operations.
 */
@Repository
public interface PermissionRepository
        extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    /**
     * Finds a non-deleted permission by UUID.
     *
     * @param uuid public UUID
     * @return optional permission
     */
    Optional<Permission> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds non-deleted permissions by UUIDs.
     *
     * @param uuids permission UUIDs
     * @return matching permissions
     */
    List<Permission> findByUuidInAndDeletedFalse(Collection<UUID> uuids);

    /**
     * Finds all non-deleted active permissions ordered by module and name.
     *
     * @param status entity status
     * @return permissions
     */
    List<Permission> findByDeletedFalseAndStatusOrderByModuleAscNameAsc(EntityStatus status);

    /**
     * Finds all non-deleted permissions ordered by module and name.
     *
     * @return permissions
     */
    List<Permission> findByDeletedFalseOrderByModuleAscNameAsc();
}
