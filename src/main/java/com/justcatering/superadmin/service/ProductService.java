package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ProductCreateRequest;
import com.justcatering.superadmin.dto.request.ProductNameCreateRequest;
import com.justcatering.superadmin.dto.request.ProductTypeCreateRequest;
import com.justcatering.superadmin.dto.request.ProductUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.ProductDetailsResponse;
import com.justcatering.superadmin.dto.response.ProductDropdownResponse;
import com.justcatering.superadmin.dto.response.ProductListResponse;
import com.justcatering.superadmin.dto.response.ProductNameResponse;
import com.justcatering.superadmin.dto.response.ProductTypeResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Product catalog service contract.
 */
public interface ProductService {

    /**
     * Creates a product.
     *
     * @param request create payload
     * @return created product details
     */
    ProductDetailsResponse create(ProductCreateRequest request);

    /**
     * Updates a product.
     *
     * @param uuid    product UUID
     * @param request update payload
     * @return updated product details
     */
    ProductDetailsResponse update(UUID uuid, ProductUpdateRequest request);

    /**
     * Returns product details by UUID.
     *
     * @param uuid product UUID
     * @return product details
     */
    ProductDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a product.
     *
     * @param uuid product UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters products.
     *
     * @param search      free-text search
     * @param productType type filter
     * @param status      status filter
     * @param pageable    pagination
     * @return page of products
     */
    PageResponse<ProductListResponse> search(
            String search,
            String productType,
            EntityStatus status,
            Pageable pageable
    );

    /**
     * Returns active products for dropdowns.
     *
     * @return dropdown options
     */
    List<ProductDropdownResponse> dropdown();

    /**
     * Returns active product types for the Type dropdown.
     *
     * @return product types
     */
    List<ProductTypeResponse> listTypes();

    /**
     * Creates a product type.
     *
     * @param request create payload
     * @return created product type
     */
    ProductTypeResponse createType(ProductTypeCreateRequest request);

    /**
     * Returns active product names for a type.
     *
     * @param productTypeUuid product type UUID
     * @return product names
     */
    List<ProductNameResponse> listNames(UUID productTypeUuid);

    /**
     * Creates a product name under a type.
     *
     * @param request create payload
     * @return created product name
     */
    ProductNameResponse createName(ProductNameCreateRequest request);
}
