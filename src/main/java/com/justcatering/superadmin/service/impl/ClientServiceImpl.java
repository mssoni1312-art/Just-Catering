package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.ClientCreateRequest;
import com.justcatering.superadmin.dto.request.ClientUpdateRequest;
import com.justcatering.superadmin.dto.response.ClientDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.Lead;
import com.justcatering.superadmin.entity.Product;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.ClientMapper;
import com.justcatering.superadmin.repository.ClientManagerAssignmentRepository;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.LeadRepository;
import com.justcatering.superadmin.repository.ProductRepository;
import com.justcatering.superadmin.service.ClientService;
import com.justcatering.superadmin.specification.ClientSpecification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ClientService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientManagerAssignmentRepository assignmentRepository;
    private final LeadRepository leadRepository;
    private final ProductRepository productRepository;
    private final ClientMapper clientMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientDetailsResponse create(ClientCreateRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (clientRepository.existsByEmailIgnoreCaseAndDeletedFalse(email)) {
            throw new BusinessException("Client email already exists", "CLIENT_EMAIL_EXISTS");
        }

        Product product = resolveActiveProduct(request.getPurchasedProductId());

        Client client = Client.builder()
                .clientName(request.getClientName().trim())
                .contactPerson(request.getContactPerson().trim())
                .mobile(normalizeOptional(request.getMobileNumber()))
                .email(email)
                .gstNumber(normalizeOptional(request.getGstNumber()))
                .clientType(request.getClientType().trim())
                .product(product)
                .dealDate(request.getConversionDate() != null ? request.getConversionDate() : LocalDate.now())
                .totalAmount(defaultAmount(request.getReward()))
                .budget(resolveBudget(request.getBudget(), request.getReward()))
                .notes(normalizeOptional(request.getNotes()))
                .clientStage(request.getClientStage() != null ? request.getClientStage() : ClientStage.ACTIVE)
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .requirementsCompletionPercentage(
                        request.getRequirementsCompletionPercentage() != null
                                ? request.getRequirementsCompletionPercentage()
                                : BigDecimal.ZERO
                )
                .state(normalizeOptional(request.getState()))
                .city(normalizeOptional(request.getCity()))
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        client = clientRepository.save(client);
        markLeadConverted(request.getLeadUuid());
        log.info("Created client: {}", client.getEmail());
        return toDetailsResponse(reloadWithProduct(client.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientDetailsResponse update(UUID uuid, ClientUpdateRequest request) {
        Client client = findOrThrow(uuid);
        String email = request.getEmail().trim().toLowerCase();

        if (clientRepository.existsByEmailIgnoreCaseAndDeletedFalseAndIdNot(email, client.getId())) {
            throw new BusinessException("Client email already exists", "CLIENT_EMAIL_EXISTS");
        }

        client.setClientName(request.getClientName().trim());
        client.setContactPerson(request.getContactPerson().trim());
        client.setMobile(normalizeOptional(request.getMobileNumber()));
        client.setEmail(email);
        client.setGstNumber(normalizeOptional(request.getGstNumber()));
        client.setClientType(request.getClientType().trim());
        client.setProduct(resolveActiveProduct(request.getPurchasedProductId()));
        client.setDealDate(request.getConversionDate());
        client.setTotalAmount(defaultAmount(request.getReward()));
        client.setBudget(resolveBudget(request.getBudget(), request.getReward()));
        client.setNotes(normalizeOptional(request.getNotes()));
        if (request.getClientStage() != null) {
            client.setClientStage(request.getClientStage());
        }
        if (request.getPriority() != null) {
            client.setPriority(request.getPriority());
        }
        if (request.getRequirementsCompletionPercentage() != null) {
            client.setRequirementsCompletionPercentage(request.getRequirementsCompletionPercentage());
        }
        client.setState(normalizeOptional(request.getState()));
        client.setCity(normalizeOptional(request.getCity()));
        if (request.getStatus() != null) {
            client.setStatus(request.getStatus());
        }

        clientRepository.save(client);
        log.info("Updated client: {}", client.getEmail());
        return toDetailsResponse(reloadWithProduct(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ClientDetailsResponse getByUuid(UUID uuid) {
        return toDetailsResponse(reloadWithProduct(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Client client = findOrThrow(uuid);
        client.softDelete();
        clientRepository.save(client);
        log.info("Soft-deleted client: {}", client.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientListResponse> search(
            String search,
            EntityStatus status,
            ClientStage clientStage,
            Priority priority,
            UUID productUuid,
            String clientType,
            LocalDate dealFrom,
            LocalDate dealTo,
            Pageable pageable
    ) {
        Page<Client> page = clientRepository.findAll(
                ClientSpecification.filter(
                        search, status, clientStage, priority, productUuid, clientType, dealFrom, dealTo
                ),
                pageable
        );
        List<Client> clients = page.getContent();
        Map<UUID, BigDecimal> rewardsByClientUuid = resolveAssignmentRewards(
                clients.stream().map(Client::getUuid).toList()
        );

        return PageResponse.from(page.map(client -> {
            ClientListResponse response = clientMapper.toList(client);
            response.setReward(rewardsByClientUuid.get(client.getUuid()));
            return response;
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClientDropdownResponse> dropdown() {
        return clientRepository.findByDeletedFalseAndStatusOrderByClientNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(clientMapper::toDropdown)
                .toList();
    }

    private Product resolveActiveProduct(UUID productUuid) {
        Product product = productRepository.findByUuidAndDeletedFalse(productUuid)
                .orElseThrow(() -> new EntityNotFoundException("Product", productUuid));
        if (product.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected product is not active");
        }
        return product;
    }

    private Client findOrThrow(UUID uuid) {
        return clientRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", uuid));
    }

    private Client reloadWithProduct(UUID uuid) {
        return clientRepository.findByUuidWithProduct(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", uuid));
    }

    private BigDecimal defaultAmount(BigDecimal amount) {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    private BigDecimal resolveBudget(BigDecimal budget, BigDecimal reward) {
        return budget != null ? budget : reward;
    }

    private ClientDetailsResponse toDetailsResponse(Client client) {
        ClientDetailsResponse response = clientMapper.toDetails(client);
        response.setReward(resolveAssignmentReward(client.getUuid()));
        return response;
    }

    private BigDecimal resolveAssignmentReward(UUID clientUuid) {
        return assignmentRepository.maxRewardAmountByClientUuid(clientUuid).orElse(null);
    }

    private Map<UUID, BigDecimal> resolveAssignmentRewards(List<UUID> clientUuids) {
        if (clientUuids.isEmpty()) {
            return Map.of();
        }

        return assignmentRepository.maxRewardAmountsByClientUuids(clientUuids)
                .stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (BigDecimal) row[1]
                ));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void markLeadConverted(UUID leadUuid) {
        if (leadUuid == null) {
            return;
        }

        Lead lead = leadRepository.findByUuidAndDeletedFalse(leadUuid)
                .orElseThrow(() -> new EntityNotFoundException("Lead", leadUuid));

        if (lead.getLeadStage() == LeadStage.CONVERTED) {
            throw new BusinessException("Lead has already been converted", "LEAD_ALREADY_CONVERTED");
        }
        if (lead.getLeadStage() == LeadStage.LOST) {
            throw new BusinessException("Cannot convert a lost lead", "LEAD_IS_LOST");
        }

        lead.setLeadStage(LeadStage.CONVERTED);
        leadRepository.save(lead);
        log.info("Marked lead {} as converted after client creation", lead.getEmail());
    }
}
