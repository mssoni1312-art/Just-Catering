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
        String email = request.getEmail().trim().toLowerCase();
        if (leadRepository.existsByEmailIgnoreCaseAndDeletedFalse(email)) {
            throw new BusinessException("Lead email already exists", "LEAD_EMAIL_EXISTS");
        }

        Lead lead = Lead.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(email)
                .companyName(request.getCompanyName().trim())
                .phone(request.getPhone().trim())
                .state(request.getState().trim())
                .city(request.getCity().trim())
                .approxBudget(request.getApproxBudget())
                .product(resolveOptionalProduct(request.getProductUuid()))
                .notes(normalizeOptional(request.getNotes()))
                .leadStage(request.getLeadStage() != null ? request.getLeadStage() : LeadStage.NEW)
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        lead = leadRepository.save(lead);
        log.info("Created lead: {}", lead.getEmail());
        return leadMapper.toDetails(reloadWithProduct(lead.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LeadDetailsResponse update(UUID uuid, LeadUpdateRequest request) {
        Lead lead = findOrThrow(uuid);
        String email = request.getEmail().trim().toLowerCase();

        if (leadRepository.existsByEmailIgnoreCaseAndDeletedFalseAndIdNot(email, lead.getId())) {
            throw new BusinessException("Lead email already exists", "LEAD_EMAIL_EXISTS");
        }

        lead.setFirstName(request.getFirstName().trim());
        lead.setLastName(request.getLastName().trim());
        lead.setEmail(email);
        lead.setCompanyName(request.getCompanyName().trim());
        lead.setPhone(request.getPhone().trim());
        lead.setState(request.getState().trim());
        lead.setCity(request.getCity().trim());
        lead.setApproxBudget(request.getApproxBudget());
        lead.setProduct(resolveOptionalProduct(request.getProductUuid()));
        lead.setNotes(normalizeOptional(request.getNotes()));
        if (request.getLeadStage() != null) {
            lead.setLeadStage(request.getLeadStage());
        }
        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }

        leadRepository.save(lead);
        log.info("Updated lead: {}", lead.getEmail());
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
        log.info("Soft-deleted lead: {}", lead.getEmail());
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

    private Product resolveOptionalProduct(UUID productUuid) {
        if (productUuid == null) {
            return null;
        }
        Product product = productRepository.findByUuidAndDeletedFalse(productUuid)
                .orElseThrow(() -> new EntityNotFoundException("Product", productUuid));
        if (product.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected product is not active");
        }
        return product;
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
}
