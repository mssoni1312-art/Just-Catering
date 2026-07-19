package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.ManagerAssignmentCreateRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentMemberRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentSyncRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentUpdateRequest;
import com.justcatering.superadmin.dto.response.ManagerAssignmentDetailsResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentListResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentSyncResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.ClientManagerAssignment;
import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.ManagerAssignmentMapper;
import com.justcatering.superadmin.repository.ClientManagerAssignmentRepository;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.DepartmentRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.ManagerAssignmentService;
import com.justcatering.superadmin.specification.ManagerAssignmentSpecification;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ManagerAssignmentService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagerAssignmentServiceImpl implements ManagerAssignmentService {

    private final ClientManagerAssignmentRepository assignmentRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ManagerAssignmentMapper assignmentMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagerAssignmentDetailsResponse create(ManagerAssignmentCreateRequest request) {
        Client client = resolveActiveClient(request.getClientUuid());
        User user = resolveActiveUser(request.getUserUuid());
        ensureUniqueAssignment(client.getId(), user.getId(), null);

        ClientManagerAssignment assignment = ClientManagerAssignment.builder()
                .client(client)
                .user(user)
                .department(resolveOptionalDepartment(request.getDepartmentUuid()))
                .projectName(normalizeOptional(request.getProjectName()))
                .closeDate(request.getCloseDate())
                .rewardAmount(request.getRewardAmount())
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        assignment = assignmentRepository.save(assignment);
        log.info("Created manager assignment for client {} and user {}", client.getClientName(), user.getFullName());
        return toDetailsResponse(reloadWithRelations(assignment.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagerAssignmentSyncResponse sync(ManagerAssignmentSyncRequest request) {
        Client client = resolveActiveClient(request.getClientUuid());
        validateUniqueMembers(request.getMembers());

        List<ClientManagerAssignment> existing =
                assignmentRepository.findActiveByClientUuid(client.getUuid());
        Map<UUID, ClientManagerAssignment> existingByUserUuid = existing.stream()
                .collect(Collectors.toMap(assignment -> assignment.getUser().getUuid(), Function.identity()));

        Set<UUID> requestedUserUuids = new HashSet<>();
        String projectName = normalizeOptional(request.getProjectName());

        for (ManagerAssignmentMemberRequest memberRequest : request.getMembers()) {
            User user = resolveActiveUser(memberRequest.getUserUuid());
            if (!requestedUserUuids.add(user.getUuid())) {
                throw new BusinessException("Duplicate member in assignment list");
            }

            Department department = resolveOptionalDepartment(memberRequest.getDepartmentUuid());
            ClientManagerAssignment assignment = existingByUserUuid.get(user.getUuid());
            if (assignment != null) {
                assignment.setDepartment(department);
                assignment.setProjectName(projectName);
                assignment.setCloseDate(request.getCloseDate());
                assignment.setRewardAmount(request.getRewardAmount());
                assignmentRepository.save(assignment);
                continue;
            }

            ClientManagerAssignment created = ClientManagerAssignment.builder()
                    .client(client)
                    .user(user)
                    .department(department)
                    .projectName(projectName)
                    .closeDate(request.getCloseDate())
                    .rewardAmount(request.getRewardAmount())
                    .status(EntityStatus.ACTIVE)
                    .deleted(Boolean.FALSE)
                    .build();
            assignmentRepository.save(created);
        }

        for (ClientManagerAssignment assignment : existing) {
            if (!requestedUserUuids.contains(assignment.getUser().getUuid())) {
                assignment.softDelete();
                assignmentRepository.save(assignment);
            }
        }

        List<ManagerAssignmentListResponse> assignments = toListResponses(
                assignmentRepository.findActiveByClientUuid(client.getUuid())
        );

        log.info("Synced {} manager assignments for client {}", assignments.size(), client.getClientName());

        return ManagerAssignmentSyncResponse.builder()
                .clientUuid(client.getUuid())
                .projectName(projectName)
                .closeDate(request.getCloseDate())
                .rewardAmount(request.getRewardAmount())
                .totalCount(assignments.size())
                .assignments(assignments)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagerAssignmentDetailsResponse update(UUID uuid, ManagerAssignmentUpdateRequest request) {
        ClientManagerAssignment assignment = findOrThrow(uuid);
        Client client = resolveActiveClient(request.getClientUuid());
        User user = resolveActiveUser(request.getUserUuid());
        ensureUniqueAssignment(client.getId(), user.getId(), assignment.getId());

        assignment.setClient(client);
        assignment.setUser(user);
        assignment.setDepartment(resolveOptionalDepartment(request.getDepartmentUuid()));
        assignment.setProjectName(normalizeOptional(request.getProjectName()));
        assignment.setCloseDate(request.getCloseDate());
        assignment.setRewardAmount(request.getRewardAmount());
        if (request.getStatus() != null) {
            assignment.setStatus(request.getStatus());
        }

        assignmentRepository.save(assignment);
        log.info("Updated manager assignment: {}", assignment.getUuid());
        return toDetailsResponse(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ManagerAssignmentDetailsResponse getByUuid(UUID uuid) {
        return toDetailsResponse(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        ClientManagerAssignment assignment = findOrThrow(uuid);
        assignment.softDelete();
        assignmentRepository.save(assignment);
        log.info("Soft-deleted manager assignment: {}", assignment.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ManagerAssignmentListResponse> search(
            String search,
            UUID clientUuid,
            UUID userUuid,
            UUID departmentUuid,
            Pageable pageable
    ) {
        Page<ClientManagerAssignment> page = assignmentRepository.findAll(
                ManagerAssignmentSpecification.filter(search, clientUuid, userUuid, departmentUuid),
                pageable
        );
        Map<UUID, Integer> memberCounts = loadMemberCounts(page.getContent());
        return PageResponse.from(page.map(assignment -> toListResponse(
                assignment,
                memberCounts.getOrDefault(assignment.getClient().getUuid(), 1)
        )));
    }

    private void ensureUniqueAssignment(Long clientId, Long userId, Long excludeId) {
        boolean exists = excludeId == null
                ? assignmentRepository.existsByClientIdAndUserIdAndDeletedFalse(clientId, userId)
                : assignmentRepository.existsByClientIdAndUserIdAndDeletedFalseAndIdNot(clientId, userId, excludeId);
        if (exists) {
            throw new BusinessException("This user is already assigned to the client");
        }
    }

    private Client resolveActiveClient(UUID clientUuid) {
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected client is not active");
        }
        return client;
    }

    private User resolveActiveUser(UUID userUuid) {
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

    private ClientManagerAssignment findOrThrow(UUID uuid) {
        return assignmentRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("ClientManagerAssignment", uuid));
    }

    private ClientManagerAssignment reloadWithRelations(UUID uuid) {
        return assignmentRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("ClientManagerAssignment", uuid));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void validateUniqueMembers(List<ManagerAssignmentMemberRequest> members) {
        Set<UUID> seen = new HashSet<>();
        for (ManagerAssignmentMemberRequest member : members) {
            if (!seen.add(member.getUserUuid())) {
                throw new BusinessException("Duplicate member in assignment list");
            }
        }
    }

    private List<BigDecimal> splitRewardAmount(BigDecimal totalReward, int memberCount) {
        if (memberCount <= 0) {
            return List.of();
        }
        if (totalReward == null) {
            return java.util.Collections.nCopies(memberCount, null);
        }
        if (memberCount == 1) {
            return List.of(totalReward);
        }

        BigDecimal baseShare = totalReward.divide(
                BigDecimal.valueOf(memberCount), 2, RoundingMode.DOWN
        );
        BigDecimal remainder = totalReward.subtract(baseShare.multiply(BigDecimal.valueOf(memberCount)));
        int remainderPaise = remainder.movePointRight(2).intValue();

        List<BigDecimal> shares = new ArrayList<>(memberCount);
        for (int i = 0; i < memberCount; i++) {
            BigDecimal share = baseShare;
            if (i < remainderPaise) {
                share = share.add(new BigDecimal("0.01"));
            }
            shares.add(share);
        }
        return shares;
    }

    private ManagerAssignmentDetailsResponse toDetailsResponse(ClientManagerAssignment assignment) {
        ManagerAssignmentDetailsResponse response = assignmentMapper.toDetails(assignment);
        int memberCount = (int) assignmentRepository.countActiveByClientUuid(assignment.getClient().getUuid());
        response.setRewardAmount(perMemberReward(response.getRewardAmount(), memberCount));
        return response;
    }

    private List<ManagerAssignmentListResponse> toListResponses(List<ClientManagerAssignment> assignments) {
        if (assignments.isEmpty()) {
            return List.of();
        }

        Map<UUID, Integer> memberCounts = loadMemberCounts(assignments);
        return assignments.stream()
                .map(assignment -> toListResponse(
                        assignment,
                        memberCounts.getOrDefault(assignment.getClient().getUuid(), 1)
                ))
                .toList();
    }

    private ManagerAssignmentListResponse toListResponse(ClientManagerAssignment assignment, int memberCount) {
        ManagerAssignmentListResponse response = assignmentMapper.toList(assignment);
        response.setRewardAmount(perMemberReward(response.getRewardAmount(), memberCount));
        return response;
    }

    private Map<UUID, Integer> loadMemberCounts(List<ClientManagerAssignment> assignments) {
        Set<UUID> clientUuids = assignments.stream()
                .map(assignment -> assignment.getClient().getUuid())
                .collect(Collectors.toSet());
        if (clientUuids.isEmpty()) {
            return Map.of();
        }

        return assignmentRepository.countActiveByClientUuids(clientUuids)
                .stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> ((Long) row[1]).intValue()
                ));
    }

    private BigDecimal perMemberReward(BigDecimal totalReward, int memberCount) {
        if (totalReward == null || memberCount <= 1) {
            return totalReward;
        }
        return splitRewardAmount(totalReward, memberCount).getFirst();
    }
}
