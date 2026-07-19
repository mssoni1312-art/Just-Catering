package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.ClientDeadlineCreateRequest;
import com.justcatering.superadmin.dto.response.ClientDeadlineDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDeadlineListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.ClientDeadline;
import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.ClientDeadlineMapper;
import com.justcatering.superadmin.repository.ClientDeadlineRepository;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.DepartmentRepository;
import com.justcatering.superadmin.service.ClientDeadlineService;
import com.justcatering.superadmin.specification.ClientDeadlineSpecification;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ClientDeadlineService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientDeadlineServiceImpl implements ClientDeadlineService {

    private final ClientDeadlineRepository deadlineRepository;
    private final ClientRepository clientRepository;
    private final DepartmentRepository departmentRepository;
    private final ClientDeadlineMapper deadlineMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientDeadlineDetailsResponse create(ClientDeadlineCreateRequest request) {
        ClientDeadline deadline = ClientDeadline.builder()
                .client(resolveActiveClient(request.getClientUuid()))
                .department(resolveOptionalDepartment(request.getDepartmentUuid()))
                .currentDeadline(request.getCurrentDeadline())
                .newDeadline(request.getNewDeadline())
                .reason(request.getReason().trim())
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        deadline = deadlineRepository.save(deadline);
        log.info("Recorded deadline change for client {}", deadline.getClient().getClientName());
        return deadlineMapper.toDetails(reloadWithRelations(deadline.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ClientDeadlineDetailsResponse getByUuid(UUID uuid) {
        return deadlineMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientDeadlineListResponse> search(
            String search,
            UUID clientUuid,
            UUID departmentUuid,
            Pageable pageable
    ) {
        Page<ClientDeadline> page = deadlineRepository.findAll(
                ClientDeadlineSpecification.filter(search, clientUuid, departmentUuid),
                pageable
        );
        return PageResponse.from(page.map(deadlineMapper::toList));
    }

    private Client resolveActiveClient(UUID clientUuid) {
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected client is not active");
        }
        return client;
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

    private ClientDeadline reloadWithRelations(UUID uuid) {
        return deadlineRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("ClientDeadline", uuid));
    }
}
