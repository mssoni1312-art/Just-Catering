package com.justcatering.superadmin.service.impl;

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
import com.justcatering.superadmin.entity.Product;
import com.justcatering.superadmin.entity.ProductName;
import com.justcatering.superadmin.entity.ProductType;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.ProductCatalogMapper;
import com.justcatering.superadmin.mapper.ProductMapper;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.ProductNameRepository;
import com.justcatering.superadmin.repository.ProductRepository;
import com.justcatering.superadmin.repository.ProductTypeRepository;
import com.justcatering.superadmin.service.ProductService;
import com.justcatering.superadmin.specification.ProductSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link ProductService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final BigDecimal DEFAULT_REWARD = new BigDecimal("5000.00");

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductNameRepository productNameRepository;
    private final ClientRepository clientRepository;
    private final ProductMapper productMapper;
    private final ProductCatalogMapper productCatalogMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductDetailsResponse create(ProductCreateRequest request) {
        String name = request.getName().trim();
        String productType = request.getProductType().trim();

        if (productRepository.existsByNameIgnoreCaseAndProductTypeIgnoreCaseAndDeletedFalse(name, productType)) {
            throw new BusinessException(
                    "Product with the same name and type already exists",
                    "PRODUCT_NAME_TYPE_EXISTS"
            );
        }

        String code = resolveCode(request.getCode(), name);
        if (productRepository.existsByCodeAndDeletedFalse(code)) {
            throw new BusinessException("Product code already exists", "PRODUCT_CODE_EXISTS");
        }

        EntityStatus status = request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE;
        BigDecimal reward = request.getDefaultRewardAmount() != null
                ? request.getDefaultRewardAmount()
                : DEFAULT_REWARD;

        Product product = Product.builder()
                .name(name)
                .code(code)
                .productType(productType)
                .description(normalizeOptional(request.getDescription()))
                .defaultRewardAmount(reward)
                .status(status)
                .deleted(Boolean.FALSE)
                .build();

        product = productRepository.save(product);
        log.info("Created product: {}", product.getCode());
        return productMapper.toDetails(product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductDetailsResponse update(UUID uuid, ProductUpdateRequest request) {
        Product product = findOrThrow(uuid);
        String name = request.getName().trim();
        String productType = request.getProductType().trim();

        if (productRepository.existsByNameIgnoreCaseAndProductTypeIgnoreCaseAndDeletedFalseAndIdNot(
                name, productType, product.getId())) {
            throw new BusinessException(
                    "Product with the same name and type already exists",
                    "PRODUCT_NAME_TYPE_EXISTS"
            );
        }

        product.setName(name);
        product.setProductType(productType);
        product.setDescription(normalizeOptional(request.getDescription()));
        if (request.getDefaultRewardAmount() != null) {
            product.setDefaultRewardAmount(request.getDefaultRewardAmount());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }

        product = productRepository.save(product);
        log.info("Updated product: {}", product.getCode());
        return productMapper.toDetails(product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDetailsResponse getByUuid(UUID uuid) {
        return productMapper.toDetails(findOrThrow(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Product product = findOrThrow(uuid);
        long linkedClients = clientRepository.countByProductIdAndDeletedFalse(product.getId());
        if (linkedClients > 0) {
            throw new BusinessException("Cannot delete product assigned to active clients");
        }
        product.softDelete();
        productRepository.save(product);
        log.info("Soft-deleted product: {}", product.getCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductListResponse> search(
            String search,
            String productType,
            EntityStatus status,
            Pageable pageable
    ) {
        Page<Product> page = productRepository.findAll(
                ProductSpecification.filter(search, productType, status),
                pageable
        );
        return PageResponse.from(page.map(productMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductDropdownResponse> dropdown() {
        return productRepository.findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(productMapper::toDropdown)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeResponse> listTypes() {
        return productTypeRepository.findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(productCatalogMapper::toTypeResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductTypeResponse createType(ProductTypeCreateRequest request) {
        String name = request.getName().trim();
        if (productTypeRepository.existsByNameIgnoreCaseAndDeletedFalse(name)) {
            throw new BusinessException("Product type already exists", "PRODUCT_TYPE_EXISTS");
        }

        ProductType productType = ProductType.builder()
                .name(name)
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        productType = productTypeRepository.save(productType);
        log.info("Created product type: {}", productType.getName());
        return productCatalogMapper.toTypeResponse(productType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductNameResponse> listNames(UUID productTypeUuid) {
        ProductType productType = findTypeOrThrow(productTypeUuid);
        return productNameRepository
                .findByProductTypeIdAndDeletedFalseAndStatusOrderByNameAsc(
                        productType.getId(),
                        EntityStatus.ACTIVE
                )
                .stream()
                .map(productCatalogMapper::toNameResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductNameResponse createName(ProductNameCreateRequest request) {
        ProductType productType = findTypeOrThrow(request.getProductTypeUuid());
        String name = request.getName().trim();

        if (productNameRepository.existsByProductTypeIdAndNameIgnoreCaseAndDeletedFalse(
                productType.getId(), name)) {
            throw new BusinessException(
                    "Product name already exists for this type",
                    "PRODUCT_NAME_EXISTS"
            );
        }

        ProductName productName = ProductName.builder()
                .productType(productType)
                .name(name)
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        productName = productNameRepository.save(productName);
        log.info("Created product name: {} ({})", productName.getName(), productType.getName());
        return productCatalogMapper.toNameResponse(productName);
    }

    private Product findOrThrow(UUID uuid) {
        return productRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Product", uuid));
    }

    private ProductType findTypeOrThrow(UUID uuid) {
        return productTypeRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Product type", uuid));
    }

    private String resolveCode(String requestedCode, String name) {
        if (StringUtils.hasText(requestedCode)) {
            return requestedCode.trim().toUpperCase();
        }
        String base = name.trim().toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_|_$", "");
        if (!StringUtils.hasText(base)) {
            base = "PRODUCT";
        }
        if (base.length() > 40) {
            base = base.substring(0, 40);
        }
        String candidate = base;
        int suffix = 1;
        while (productRepository.existsByCodeAndDeletedFalse(candidate)) {
            candidate = base + "_" + suffix++;
        }
        return candidate;
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
