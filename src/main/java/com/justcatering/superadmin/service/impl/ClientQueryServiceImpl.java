package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.ClientQueryCreateRequest;
import com.justcatering.superadmin.dto.request.ClientQueryUpdateRequest;
import com.justcatering.superadmin.dto.response.ClientQueryDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientQueryDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientQueryListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.ClientQuery;
import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.ClientQueryMapper;
import com.justcatering.superadmin.repository.ClientQueryRepository;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.DepartmentRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.ClientQueryService;
import com.justcatering.superadmin.specification.ClientQuerySpecification;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ClientQueryService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientQueryServiceImpl implements ClientQueryService {

    private final ClientQueryRepository clientQueryRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ClientQueryMapper clientQueryMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientQueryDetailsResponse create(ClientQueryCreateRequest request) {
        QueryStatus queryStatus = request.getQueryStatus() != null ? request.getQueryStatus() : QueryStatus.PENDING;

        ClientQuery clientQuery = ClientQuery.builder()
                .client(resolveActiveClient(request.getClientUuid()))
                .title(request.getTitle().trim())
                .queryType(normalizeOptional(request.getQueryType()))
                .assignedUser(resolveOptionalUser(request.getAssignedUserUuid()))
                .department(resolveOptionalDepartment(request.getDepartmentUuid()))
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .queryStatus(queryStatus)
                .remarks(normalizeOptional(request.getRemarks()))
                .imageUrl(normalizeOptional(request.getImageUrl()))
                .completedAt(resolveCompletedAt(queryStatus, request.getCompletedAt()))
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        clientQuery = clientQueryRepository.save(clientQuery);
        log.info("Created client query: {}", clientQuery.getTitle());
        return clientQueryMapper.toDetails(reloadWithRelations(clientQuery.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientQueryDetailsResponse update(UUID uuid, ClientQueryUpdateRequest request) {
        ClientQuery clientQuery = findOrThrow(uuid);

        clientQuery.setClient(resolveActiveClient(request.getClientUuid()));
        clientQuery.setTitle(request.getTitle().trim());
        clientQuery.setQueryType(normalizeOptional(request.getQueryType()));
        clientQuery.setAssignedUser(resolveOptionalUser(request.getAssignedUserUuid()));
        clientQuery.setDepartment(resolveOptionalDepartment(request.getDepartmentUuid()));
        if (request.getPriority() != null) {
            clientQuery.setPriority(request.getPriority());
        }
        if (request.getQueryStatus() != null) {
            clientQuery.setQueryStatus(request.getQueryStatus());
            clientQuery.setCompletedAt(resolveCompletedAt(request.getQueryStatus(), request.getCompletedAt()));
        } else if (request.getCompletedAt() != null) {
            clientQuery.setCompletedAt(request.getCompletedAt());
        }
        clientQuery.setRemarks(normalizeOptional(request.getRemarks()));
        clientQuery.setImageUrl(normalizeOptional(request.getImageUrl()));
        if (request.getStatus() != null) {
            clientQuery.setStatus(request.getStatus());
        }

        clientQueryRepository.save(clientQuery);
        log.info("Updated client query: {}", clientQuery.getTitle());
        return clientQueryMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ClientQueryDetailsResponse getByUuid(UUID uuid) {
        return clientQueryMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        ClientQuery clientQuery = findOrThrow(uuid);
        clientQuery.softDelete();
        clientQueryRepository.save(clientQuery);
        log.info("Soft-deleted client query: {}", clientQuery.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientQueryListResponse> search(
            String search,
            QueryStatus queryStatus,
            Priority priority,
            UUID clientUuid,
            UUID assignedUserUuid,
            UUID departmentUuid,
            Pageable pageable
    ) {
        Page<ClientQuery> page = clientQueryRepository.findAll(
                ClientQuerySpecification.filter(
                        search, queryStatus, priority, clientUuid, assignedUserUuid, departmentUuid
                ),
                pageable
        );
        return PageResponse.from(page.map(clientQueryMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClientQueryDropdownResponse> dropdown() {
        return clientQueryRepository
                .findByDeletedFalseAndStatusOrderByCreatedAtDescTitleAsc(EntityStatus.ACTIVE)
                .stream()
                .map(clientQueryMapper::toDropdown)
                .toList();
    }

    private Client resolveActiveClient(UUID clientUuid) {
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected client is not active");
        }
        return client;
    }

    private User resolveOptionalUser(UUID userUuid) {
        if (userUuid == null) {
            return null;
        }
        User user = userRepository.findByUuidAndDeletedFalse(userUuid)
                .orElseThrow(() -> new EntityNotFoundException("User", userUuid));
        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected user is not active");
        }
        return user;
    }

    private Department resolveOptionalDepartment(UUID departmentUuid) {
        if (departmentUuid == null) {
            return null;
        }
        Department department = departmentRepository.findByUuidAndDeletedFalse(departmentUuid)
                .orElseThrow(() -> new EntityNotFoundException("Department", departmentUuid));
        if (department.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected department is not active");
        }
        return department;
    }

    private Instant resolveCompletedAt(QueryStatus queryStatus, Instant completedAt) {
        if (isTerminalStatus(queryStatus)) {
            return completedAt != null ? completedAt : Instant.now();
        }
        return null;
    }

    private boolean isTerminalStatus(QueryStatus queryStatus) {
        return queryStatus == QueryStatus.COMPLETED || queryStatus == QueryStatus.SOLVED;
    }

    private ClientQuery findOrThrow(UUID uuid) {
        return clientQueryRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("ClientQuery", uuid));
    }

    private ClientQuery reloadWithRelations(UUID uuid) {
        return clientQueryRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("ClientQuery", uuid));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
