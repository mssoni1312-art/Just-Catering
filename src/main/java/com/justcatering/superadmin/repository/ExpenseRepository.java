package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Expense;
import com.justcatering.superadmin.enums.ExpenseType;
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
 * Repository for {@link Expense} persistence operations.
 */
@Repository
public interface ExpenseRepository
        extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    /**
     * Finds a non-deleted expense by UUID.
     *
     * @param uuid public UUID
     * @return optional expense
     */
    Optional<Expense> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Finds an expense with client and member user by UUID.
     *
     * @param uuid public UUID
     * @return optional expense
     */
    @Query("""
            SELECT e FROM Expense e
            LEFT JOIN FETCH e.client c
            LEFT JOIN FETCH e.memberUser u
            WHERE e.uuid = :uuid
              AND e.deleted = FALSE
            """)
    Optional<Expense> findByUuidWithRelations(@Param("uuid") UUID uuid);

    /**
     * Sums expense amounts grouped by type for summary reporting.
     *
     * @param clientUuid     optional client UUID filter
     * @param memberUserUuid optional member user UUID filter
     * @param expenseType    optional expense type filter
     * @param expenseFrom    optional expense date from
     * @param expenseTo      optional expense date to
     * @return type and total amount pairs
     */
    @Query("""
            SELECT e.expenseType, COALESCE(SUM(e.amount), 0)
            FROM Expense e
            LEFT JOIN e.client c
            LEFT JOIN e.memberUser u
            WHERE e.deleted = FALSE
              AND (:clientUuid IS NULL OR c.uuid = :clientUuid)
              AND (:memberUserUuid IS NULL OR u.uuid = :memberUserUuid)
              AND (:expenseType IS NULL OR e.expenseType = :expenseType)
              AND (:expenseFrom IS NULL OR e.expenseDate >= :expenseFrom)
              AND (:expenseTo IS NULL OR e.expenseDate <= :expenseTo)
            GROUP BY e.expenseType
            """)
    List<Object[]> sumAmountsByType(
            @Param("clientUuid") UUID clientUuid,
            @Param("memberUserUuid") UUID memberUserUuid,
            @Param("expenseType") ExpenseType expenseType,
            @Param("expenseFrom") java.time.LocalDate expenseFrom,
            @Param("expenseTo") java.time.LocalDate expenseTo
    );

    /**
     * Sums all expense amounts for summary reporting.
     *
     * @param clientUuid     optional client UUID filter
     * @param memberUserUuid optional member user UUID filter
     * @param expenseType    optional expense type filter
     * @param expenseFrom    optional expense date from
     * @param expenseTo      optional expense date to
     * @return total amount
     */
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            LEFT JOIN e.client c
            LEFT JOIN e.memberUser u
            WHERE e.deleted = FALSE
              AND (:clientUuid IS NULL OR c.uuid = :clientUuid)
              AND (:memberUserUuid IS NULL OR u.uuid = :memberUserUuid)
              AND (:expenseType IS NULL OR e.expenseType = :expenseType)
              AND (:expenseFrom IS NULL OR e.expenseDate >= :expenseFrom)
              AND (:expenseTo IS NULL OR e.expenseDate <= :expenseTo)
            """)
    BigDecimal sumTotalAmount(
            @Param("clientUuid") UUID clientUuid,
            @Param("memberUserUuid") UUID memberUserUuid,
            @Param("expenseType") ExpenseType expenseType,
            @Param("expenseFrom") java.time.LocalDate expenseFrom,
            @Param("expenseTo") java.time.LocalDate expenseTo
    );
}
