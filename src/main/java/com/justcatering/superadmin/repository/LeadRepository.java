package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Lead;
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
 * Repository for {@link Lead} persistence operations.
 */
@Repository
public interface LeadRepository
        extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    /**
     * Finds a non-deleted lead by UUID.
     *
     * @param uuid public UUID
     * @return optional lead
     */
    Optional<Lead> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a lead with product by UUID.
     *
     * @param uuid public UUID
     * @return optional lead
     */
    @Query("""
            SELECT l FROM Lead l
            LEFT JOIN FETCH l.product p
            WHERE l.uuid = :uuid
              AND l.deleted = FALSE
            """)
    Optional<Lead> findByUuidWithProduct(@Param("uuid") UUID uuid);

    /**
     * Checks whether a non-deleted lead exists with the given email.
     *
     * @param email email address
     * @return {@code true} if exists
     */
    boolean existsByEmailIgnoreCaseAndDeletedFalse(String email);

    /**
     * Checks email uniqueness excluding a lead id.
     *
     * @param email     email address
     * @param excludeId lead id to exclude
     * @return {@code true} if another lead uses the email
     */
    boolean existsByEmailIgnoreCaseAndDeletedFalseAndIdNot(String email, Long excludeId);

    /**
     * Finds active leads for dropdowns.
     *
     * @param status entity status
     * @return leads
     */
    List<Lead> findByDeletedFalseAndStatusOrderByCompanyNameAscFirstNameAsc(EntityStatus status);

    /**
     * Counts non-deleted meeting leads.
     *
     * @return lead count
     */
    long countByDeletedFalse();
}
