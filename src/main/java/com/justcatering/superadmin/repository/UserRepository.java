package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.User;
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
 * Repository for {@link User} persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Finds a non-deleted user by email.
     *
     * @param email email address
     * @return optional user
     */
    Optional<User> findByEmailIgnoreCaseAndDeletedFalse(String email);

    /**
     * Finds a non-deleted user by public UUID.
     *
     * @param uuid public UUID
     * @return optional user
     */
    Optional<User> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Checks whether a non-deleted user exists with the given email.
     *
     * @param email email address
     * @return {@code true} if exists
     */
    boolean existsByEmailIgnoreCaseAndDeletedFalse(String email);

    /**
     * Loads a user with roles and permissions for authentication.
     *
     * @param email email address
     * @return optional user with associations
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.userRoles ur
            LEFT JOIN FETCH ur.role r
            LEFT JOIN FETCH r.rolePermissions rp
            LEFT JOIN FETCH rp.permission p
            WHERE LOWER(u.email) = LOWER(:email)
              AND u.deleted = FALSE
            """)
    Optional<User> findByEmailWithRolesAndPermissions(@Param("email") String email);

    /**
     * Loads a user by id with roles and permissions.
     *
     * @param id user id
     * @return optional user with associations
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.userRoles ur
            LEFT JOIN FETCH ur.role r
            LEFT JOIN FETCH r.rolePermissions rp
            LEFT JOIN FETCH rp.permission p
            WHERE u.id = :id
              AND u.deleted = FALSE
              AND u.status = :status
            """)
    Optional<User> findActiveByIdWithRolesAndPermissions(
            @Param("id") Long id,
            @Param("status") EntityStatus status
    );

    /**
     * Loads a user by UUID with roles and permissions.
     *
     * @param uuid public UUID
     * @return optional user with associations
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.userRoles ur
            LEFT JOIN FETCH ur.role r
            LEFT JOIN FETCH r.rolePermissions rp
            LEFT JOIN FETCH rp.permission p
            WHERE u.uuid = :uuid
              AND u.deleted = FALSE
            """)
    Optional<User> findByUuidWithRolesAndPermissions(@Param("uuid") UUID uuid);

    /**
     * Finds active non-deleted users for dropdowns.
     *
     * @param status entity status
     * @return users
     */
    List<User> findByDeletedFalseAndStatusOrderByFirstNameAscLastNameAsc(EntityStatus status);
}
