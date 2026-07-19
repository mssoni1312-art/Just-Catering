package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.ProductCreateRequest;
import com.justcatering.superadmin.dto.request.ProductNameCreateRequest;
import com.justcatering.superadmin.dto.request.ProductTypeCreateRequest;
import com.justcatering.superadmin.dto.request.ProductUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.ProductDetailsResponse;
import com.justcatering.superadmin.dto.response.ProductDropdownResponse;
import com.justcatering.superadmin.dto.response.ProductListResponse;
import com.justcatering.superadmin.dto.response.ProductNameResponse;
import com.justcatering.superadmin.dto.response.ProductTypeResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.service.ProductService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for product catalog APIs.
 */
@RestController
@RequestMapping(AppConstants.PRODUCT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Products", description = "Product CRUD, search, filter, types, and dropdown APIs")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a product.
     *
     * @param request create payload
     * @return created product
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @Operation(summary = "Create product")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> create(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        ProductDetailsResponse data = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a product.
     *
     * @param uuid    product UUID
     * @param request update payload
     * @return updated product
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        ProductDetailsResponse data = productService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns product details.
     *
     * @param uuid product UUID
     * @return product details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @Operation(summary = "Get product by UUID")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        ProductDetailsResponse data = productService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a product.
     *
     * @param uuid product UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @Operation(summary = "Delete product")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        productService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists products with optional filters.
     *
     * @return page of products
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @Operation(summary = "List products")
    public ResponseEntity<ApiResponse<PageResponse<ProductListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<ProductListResponse> data = productService.search(
                search,
                productType,
                status,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches products.
     *
     * @return page of products
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @Operation(summary = "Search products")
    public ResponseEntity<ApiResponse<PageResponse<ProductListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters products.
     *
     * @return page of products
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @Operation(summary = "Filter products")
    public ResponseEntity<ApiResponse<PageResponse<ProductListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, productType, status, page, size, sortBy, direction);
    }

    /**
     * Returns active products for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('PRODUCT_VIEW', 'PRODUCT_MANAGE', 'CLIENT_CREATE', 'CLIENT_UPDATE', 'LEAD_CREATE')")
    @Operation(summary = "Product dropdown")
    public ResponseEntity<ApiResponse<List<ProductDropdownResponse>>> dropdown() {
        List<ProductDropdownResponse> data = productService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns active product types for the Type dropdown.
     *
     * @return product types
     */
    @GetMapping("/types")
    @PreAuthorize("hasAnyAuthority('PRODUCT_VIEW', 'PRODUCT_MANAGE')")
    @Operation(summary = "List product types")
    public ResponseEntity<ApiResponse<List<ProductTypeResponse>>> listTypes() {
        List<ProductTypeResponse> data = productService.listTypes();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Creates a product type.
     *
     * @param request create payload
     * @return created product type
     */
    @PostMapping("/types")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @Operation(summary = "Add product type")
    public ResponseEntity<ApiResponse<ProductTypeResponse>> createType(
            @Valid @RequestBody ProductTypeCreateRequest request
    ) {
        ProductTypeResponse data = productService.createType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Returns active product names for a product type.
     *
     * @param productTypeUuid product type UUID
     * @return product names
     */
    @GetMapping("/names")
    @PreAuthorize("hasAnyAuthority('PRODUCT_VIEW', 'PRODUCT_MANAGE')")
    @Operation(summary = "List product names")
    public ResponseEntity<ApiResponse<List<ProductNameResponse>>> listNames(
            @RequestParam UUID productTypeUuid
    ) {
        List<ProductNameResponse> data = productService.listNames(productTypeUuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Creates a product name under a product type.
     *
     * @param request create payload
     * @return created product name
     */
    @PostMapping("/names")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @Operation(summary = "Add product name")
    public ResponseEntity<ApiResponse<ProductNameResponse>> createName(
            @Valid @RequestBody ProductNameCreateRequest request
    ) {
        ProductNameResponse data = productService.createName(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }
}
