package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.LeadCreateRequest;
import com.justcatering.superadmin.dto.request.LeadUpdateRequest;
import com.justcatering.superadmin.dto.response.LeadDetailsResponse;
import com.justcatering.superadmin.dto.response.LeadDropdownResponse;
import com.justcatering.superadmin.dto.response.LeadListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Lead;
import com.justcatering.superadmin.entity.Product;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.LeadMapper;
import com.justcatering.superadmin.repository.LeadRepository;
import com.justcatering.superadmin.repository.ProductRepository;
import com.justcatering.superadmin.service.LeadService;
import com.justcatering.superadmin.specification.LeadSpecification;
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
 * Default implementation of {@link LeadService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final ProductRepository productRepository;
    private final LeadMapper leadMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public LeadDetailsResponse create(LeadCreateRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (StringUtils.hasText(email) && leadRepository.existsByEmailIgnoreCaseAndDeletedFalse(email)) {
            throw new BusinessException("Lead email already exists", "LEAD_EMAIL_EXISTS");
        }

        String ownerName = request.resolveOwnerName();
        Lead lead = Lead.builder()
                .firstName(ownerName)
                .lastName("")
                .email(email != null ? email : "")
                .companyName(request.getCompanyName().trim())
                .phone(request.getPhone().trim())
                .address(request.getAddress().trim())
                .state(request.getState().trim())
                .city(request.getCity().trim())
                .approxBudget(request.getApproxBudget())
                .product(resolveProduct(request.getProductUuid(), request.getProductName()))
                .notes(normalizeOptional(request.getNotes()))
                .leadStage(request.getLeadStage() != null ? request.getLeadStage() : LeadStage.NEW)
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        lead = leadRepository.save(lead);
        log.info("Created lead for company: {}", lead.getCompanyName());
        return leadMapper.toDetails(reloadWithProduct(lead.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LeadDetailsResponse update(UUID uuid, LeadUpdateRequest request) {
        Lead lead = findOrThrow(uuid);
        String email = normalizeEmail(request.getEmail());

        if (StringUtils.hasText(email) && leadRepository.existsByEmailIgnoreCaseAndDeletedFalseAndIdNot(email, lead.getId())) {
            throw new BusinessException("Lead email already exists", "LEAD_EMAIL_EXISTS");
        }

        lead.setFirstName(request.resolveOwnerName());
        lead.setLastName("");
        lead.setEmail(email != null ? email : "");
        lead.setCompanyName(request.getCompanyName().trim());
        lead.setPhone(request.getPhone().trim());
        lead.setAddress(request.getAddress().trim());
        lead.setState(request.getState().trim());
        lead.setCity(request.getCity().trim());
        lead.setApproxBudget(request.getApproxBudget());
        lead.setProduct(resolveProduct(request.getProductUuid(), request.getProductName()));
        lead.setNotes(normalizeOptional(request.getNotes()));
        if (request.getLeadStage() != null) {
            lead.setLeadStage(request.getLeadStage());
        }
        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }

        leadRepository.save(lead);
        log.info("Updated lead for company: {}", lead.getCompanyName());
        return leadMapper.toDetails(reloadWithProduct(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LeadDetailsResponse getByUuid(UUID uuid) {
        return leadMapper.toDetails(reloadWithProduct(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Lead lead = findOrThrow(uuid);
        lead.softDelete();
        leadRepository.save(lead);
        log.info("Soft-deleted lead for company: {}", lead.getCompanyName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<LeadListResponse> search(
            String search,
            EntityStatus status,
            LeadStage leadStage,
            UUID productUuid,
            String state,
            String city,
            BigDecimal budgetFrom,
            BigDecimal budgetTo,
            Pageable pageable
    ) {
        Page<Lead> page = leadRepository.findAll(
                LeadSpecification.filter(
                        search, status, leadStage, productUuid, state, city, budgetFrom, budgetTo
                ),
                pageable
        );
        return PageResponse.from(page.map(leadMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LeadDropdownResponse> dropdown() {
        return leadRepository.findByDeletedFalseAndStatusOrderByCompanyNameAscFirstNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(leadMapper::toDropdown)
                .toList();
    }

    private Product resolveProduct(UUID productUuid, String productName) {
        if (productUuid != null) {
            return resolveProductByUuid(productUuid);
        }
        if (StringUtils.hasText(productName)) {
            return resolveProductByName(productName.trim());
        }
        return null;
    }

    private Product resolveProductByUuid(UUID productUuid) {
        Product product = productRepository.findByUuidAndDeletedFalse(productUuid)
                .orElseThrow(() -> new EntityNotFoundException("Product", productUuid));
        if (product.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected product is not active");
        }
        return product;
    }

    private Product resolveProductByName(String productName) {
        List<Product> products = productRepository.findByNameIgnoreCaseAndDeletedFalseAndStatus(
                productName,
                EntityStatus.ACTIVE
        );
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Product with name", productName);
        }
        if (products.size() > 1) {
            throw new BusinessException(
                    "Multiple products match the given name",
                    "PRODUCT_NAME_AMBIGUOUS"
            );
        }
        return products.get(0);
    }

    private Lead findOrThrow(UUID uuid) {
        return leadRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Lead", uuid));
    }

    private Lead reloadWithProduct(UUID uuid) {
        return leadRepository.findByUuidWithProduct(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Lead", uuid));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeEmail(String value) {
        String email = normalizeOptional(value);
        return email != null ? email.toLowerCase() : null;
    }
}
