package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.Product;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Product} persistence operations.
 */
@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Finds a non-deleted product by UUID.
     *
     * @param uuid public UUID
     * @return optional product
     */
    Optional<Product> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Checks whether a non-deleted product exists with the given code.
     *
     * @param code product code
     * @return {@code true} if exists
     */
    boolean existsByCodeAndDeletedFalse(String code);

    /**
     * Checks name+type uniqueness.
     *
     * @param name        product name
     * @param productType product type
     * @return {@code true} if exists
     */
    boolean existsByNameIgnoreCaseAndProductTypeIgnoreCaseAndDeletedFalse(String name, String productType);

    /**
     * Checks name+type uniqueness excluding a product id.
     *
     * @param name        product name
     * @param productType product type
     * @param excludeId   product id to exclude
     * @return {@code true} if another product matches
     */
    boolean existsByNameIgnoreCaseAndProductTypeIgnoreCaseAndDeletedFalseAndIdNot(
            String name,
            String productType,
            Long excludeId
    );

    /**
     * Finds active products for dropdowns.
     *
     * @param status entity status
     * @return products
     */
    List<Product> findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus status);
}
