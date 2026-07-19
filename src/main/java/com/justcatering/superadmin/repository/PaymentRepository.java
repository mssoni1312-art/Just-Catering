package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Payment;
import com.justcatering.superadmin.enums.EntityStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Payment} persistence operations.
 */
@Repository
public interface PaymentRepository
        extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    /**
     * Finds a non-deleted payment by UUID.
     *
     * @param uuid public UUID
     * @return optional payment
     */
    Optional<Payment> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds a payment with client and product by UUID.
     *
     * @param uuid public UUID
     * @return optional payment
     */
    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.client c
            LEFT JOIN FETCH p.product pr
            WHERE p.uuid = :uuid
              AND p.deleted = FALSE
            """)
    Optional<Payment> findByUuidWithRelations(@Param("uuid") UUID uuid);

    /**
     * Checks whether a non-deleted payment exists with the given invoice number.
     *
     * @param invoiceNumber invoice number
     * @return {@code true} if exists
     */
    boolean existsByInvoiceNumberIgnoreCaseAndDeletedFalse(String invoiceNumber);

    /**
     * Checks invoice uniqueness excluding a payment id.
     *
     * @param invoiceNumber invoice number
     * @param excludeId     payment id to exclude
     * @return {@code true} if another payment matches
     */
    boolean existsByInvoiceNumberIgnoreCaseAndDeletedFalseAndIdNot(String invoiceNumber, Long excludeId);

    /**
     * Sums paid amounts for a client.
     *
     * @param clientId client id
     * @return total paid amount
     */
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.client.id = :clientId
              AND p.deleted = FALSE
            """)
    BigDecimal sumAmountByClientId(@Param("clientId") Long clientId);

    /**
     * Returns recent receipts for a client.
     *
     * @param clientUuid client UUID
     * @param status     entity status
     * @return recent payments
     */
    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.client c
            LEFT JOIN FETCH p.product pr
            WHERE c.uuid = :clientUuid
              AND p.deleted = FALSE
              AND p.status = :status
            ORDER BY p.paymentDate DESC, p.createdAt DESC
            """)
    List<Payment> findRecentByClientUuid(
            @Param("clientUuid") UUID clientUuid,
            @Param("status") EntityStatus status
    );

    /**
     * Finds active payments for dropdowns.
     *
     * @param status entity status
     * @return payments
     */
    List<Payment> findByDeletedFalseAndStatusOrderByPaymentDateDescInvoiceNumberAsc(EntityStatus status);

    /**
     * Sums paid amounts across all non-deleted payments.
     *
     * @return total revenue collected
     */
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.deleted = FALSE
            """)
    BigDecimal sumAmountByDeletedFalse();
}
