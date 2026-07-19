package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Department} persistence operations.
 */
@Repository
public interface DepartmentRepository
        extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    /**
     * Finds a non-deleted department by UUID.
     *
     * @param uuid public UUID
     * @return optional department
     */
    Optional<Department> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a department with members and parent by UUID.
     *
     * @param uuid public UUID
     * @return optional department
     */
    @Query("""
            SELECT DISTINCT d FROM Department d
            LEFT JOIN FETCH d.parent p
            LEFT JOIN FETCH d.members m
            LEFT JOIN FETCH m.user u
            WHERE d.uuid = :uuid
              AND d.deleted = FALSE
            """)
    Optional<Department> findByUuidWithMembers(@Param("uuid") UUID uuid);

    /**
     * Checks whether an active department exists with the given name.
     *
     * @param name department name
     * @return {@code true} if exists
     */
    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);

    /**
     * Checks name uniqueness excluding a department id.
     *
     * @param name      department name
     * @param excludeId department id to exclude
     * @return {@code true} if another department uses the name
     */
    boolean existsByNameIgnoreCaseAndDeletedFalseAndIdNot(String name, Long excludeId);

    /**
     * Checks whether a non-deleted department exists with the given code.
     *
     * @param code department code
     * @return {@code true} if exists
     */
    boolean existsByCodeAndDeletedFalse(String code);

    /**
     * Finds active departments for dropdowns.
     *
     * @param status entity status
     * @return departments
     */
    List<Department> findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus status);

    /**
     * Counts active child departments under a parent.
     *
     * @param parentId parent department id
     * @return child count
     */
    long countByParentIdAndDeletedFalse(Long parentId);
}
