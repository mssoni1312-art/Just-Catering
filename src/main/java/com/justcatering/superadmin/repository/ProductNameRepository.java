package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.ProductName;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link ProductName} persistence operations.
 */
@Repository
public interface ProductNameRepository extends JpaRepository<ProductName, Long> {

    /**
     * Finds a non-deleted product name by UUID.
     *
     * @param uuid public UUID
     * @return optional product name
     */
    Optional<ProductName> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Checks name uniqueness within a product type.
     *
     * @param productTypeId product type id
     * @param name          product name
     * @return {@code true} if exists
     */
    boolean existsByProductTypeIdAndNameIgnoreCaseAndDeletedFalse(Long productTypeId, String name);

    /**
     * Lists active product names for a type ordered by name.
     *
     * @param productTypeId product type id
     * @param status        entity status
     * @return product names
     */
    List<ProductName> findByProductTypeIdAndDeletedFalseAndStatusOrderByNameAsc(
            Long productTypeId,
            EntityStatus status
    );
}
