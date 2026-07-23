package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.ExpenseCategory;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link ExpenseCategory} persistence.
 */
@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    /**
     * Finds a non-deleted category by UUID.
     *
     * @param uuid public UUID
     * @return optional category
     */
    Optional<ExpenseCategory> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Lists active categories in display order.
     *
     * @param status entity status
     * @return categories
     */
    List<ExpenseCategory> findByDeletedFalseAndStatusOrderBySortOrderAscNameAsc(EntityStatus status);

    /**
     * Checks whether an active category already uses the name.
     *
     * @param name category name
     * @return {@code true} if exists
     */
    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);

    /**
     * Returns the next sort order value.
     *
     * @return max sort order or null
     */
    @Query("SELECT MAX(c.sortOrder) FROM ExpenseCategory c WHERE c.deleted = FALSE")
    Integer findMaxSortOrder();
}
