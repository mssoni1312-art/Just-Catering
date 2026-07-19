package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.enums.EntityStatus;
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
 * Repository for {@link Role} persistence operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * Finds a non-deleted role by code.
     *
     * @param code role code
     * @return optional role
     */
    Optional<Role> findByCodeAndDeletedFalse(String code);

    /**
     * Finds a non-deleted role by UUID.
     *
     * @param uuid public UUID
     * @return optional role
     */
    Optional<Role> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds non-deleted roles by UUIDs.
     *
     * @param uuids role UUIDs
     * @return matching roles
     */
    List<Role> findByUuidInAndDeletedFalse(Collection<UUID> uuids);

    /**
     * Checks whether a non-deleted role exists with the given code.
     *
     * @param code role code
     * @return {@code true} if exists
     */
    boolean existsByCodeAndDeletedFalse(String code);

    /**
     * Checks whether a non-deleted role exists with the given name.
     *
     * @param name role name
     * @return {@code true} if exists
     */
    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);

    /**
     * Checks name uniqueness excluding a role id.
     *
     * @param name      role name
     * @param excludeId role id to exclude
     * @return {@code true} if another role uses the name
     */
    boolean existsByNameIgnoreCaseAndDeletedFalseAndIdNot(String name, Long excludeId);

    /**
     * Loads a role with permissions by UUID.
     *
     * @param uuid public UUID
     * @return optional role
     */
    @Query("""
            SELECT DISTINCT r FROM Role r
            LEFT JOIN FETCH r.rolePermissions rp
            LEFT JOIN FETCH rp.permission p
            WHERE r.uuid = :uuid
              AND r.deleted = FALSE
            """)
    Optional<Role> findByUuidWithPermissions(@Param("uuid") UUID uuid);

    /**
     * Finds active non-deleted roles for dropdowns.
     *
     * @param status entity status
     * @return roles
     */
    List<Role> findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus status);
}
