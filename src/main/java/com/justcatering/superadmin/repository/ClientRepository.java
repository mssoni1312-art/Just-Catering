package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.enums.EntityStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Client} persistence operations.
 */
@Repository
public interface ClientRepository
        extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    /**
     * Finds a non-deleted client by UUID.
     *
     * @param uuid public UUID
     * @return optional client
     */
    Optional<Client> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a client with product by UUID.
     *
     * @param uuid public UUID
     * @return optional client
     */
    @Query("""
            SELECT c FROM Client c
            JOIN FETCH c.product p
            WHERE c.uuid = :uuid
              AND c.deleted = FALSE
            """)
    Optional<Client> findByUuidWithProduct(@Param("uuid") UUID uuid);

    /**
     * Checks whether a non-deleted client exists with the given email.
     *
     * @param email email address
     * @return {@code true} if exists
     */
    boolean existsByEmailIgnoreCaseAndDeletedFalse(String email);

    /**
     * Checks email uniqueness excluding a client id.
     *
     * @param email     email address
     * @param excludeId client id to exclude
     * @return {@code true} if another client uses the email
     */
    boolean existsByEmailIgnoreCaseAndDeletedFalseAndIdNot(String email, Long excludeId);

    /**
     * Finds active clients for dropdowns.
     *
     * @param status entity status
     * @return clients
     */
    List<Client> findByDeletedFalseAndStatusOrderByClientNameAsc(EntityStatus status);

    /**
     * Counts non-deleted clients linked to a product.
     *
     * @param productId product id
     * @return client count
     */
    long countByProductIdAndDeletedFalse(Long productId);

    /**
     * Counts non-deleted clients.
     *
     * @return client count
     */
    long countByDeletedFalse();

    /**
     * Sums deal amounts for non-deleted clients.
     *
     * @return total deal amount
     */
    @Query("""
            SELECT COALESCE(SUM(c.totalAmount), 0)
            FROM Client c
            WHERE c.deleted = FALSE
            """)
    BigDecimal sumTotalAmountByDeletedFalse();

    /**
     * Sums project budgets for non-deleted clients.
     *
     * @return total project budget
     */
    @Query("""
            SELECT COALESCE(SUM(COALESCE(c.budget, c.totalAmount, 0)), 0)
            FROM Client c
            WHERE c.deleted = FALSE
            """)
    BigDecimal sumBudgetByDeletedFalse();

    /**
     * Finds clients with a deal date inside the given inclusive range.
     *
     * @param fromDate start date
     * @param toDate   end date
     * @return clients
     */
    @Query("""
            SELECT c FROM Client c
            JOIN FETCH c.product p
            WHERE c.deleted = FALSE
              AND c.dealDate IS NOT NULL
              AND c.dealDate >= :fromDate
              AND c.dealDate <= :toDate
            ORDER BY c.dealDate ASC, c.clientName ASC
            """)
    List<Client> findActiveClientsWithDealDateBetween(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /**
     * Finds clients scheduled on a specific deal date.
     *
     * @param date deal date
     * @return clients
     */
    @Query("""
            SELECT c FROM Client c
            JOIN FETCH c.product p
            WHERE c.deleted = FALSE
              AND c.dealDate = :date
            ORDER BY c.clientName ASC
            """)
    List<Client> findActiveClientsByDealDate(@Param("date") LocalDate date);
}
