package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.ProductType;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link ProductType} persistence operations.
 */
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    /**
     * Finds a non-deleted product type by UUID.
     *
     * @param uuid public UUID
     * @return optional product type
     */
    Optional<ProductType> findByUuidAndDeletedFalse(UUID uuid);

    /**
     * Checks whether a non-deleted product type exists with the given name.
     *
     * @param name type name
     * @return {@code true} if exists
     */
    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);

    /**
     * Lists active product types ordered by name.
     *
     * @param status entity status
     * @return product types
     */
    List<ProductType> findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus status);
}
