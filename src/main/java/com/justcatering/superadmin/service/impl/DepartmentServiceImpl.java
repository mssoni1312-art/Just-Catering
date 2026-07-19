package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.DepartmentCreateRequest;
import com.justcatering.superadmin.dto.request.DepartmentMemberRequest;
import com.justcatering.superadmin.dto.request.DepartmentUpdateRequest;
import com.justcatering.superadmin.dto.response.DepartmentDetailsResponse;
import com.justcatering.superadmin.dto.response.DepartmentDropdownResponse;
import com.justcatering.superadmin.dto.response.DepartmentListResponse;
import com.justcatering.superadmin.dto.response.DepartmentMemberResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.entity.DepartmentMember;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.DepartmentMapper;
import com.justcatering.superadmin.repository.DepartmentMemberRepository;
import com.justcatering.superadmin.repository.DepartmentRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.DepartmentService;
import com.justcatering.superadmin.specification.DepartmentSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link DepartmentService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMemberRepository departmentMemberRepository;
    private final UserRepository userRepository;
    private final DepartmentMapper departmentMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentDetailsResponse create(DepartmentCreateRequest request) {
        String name = request.getName().trim();
        if (departmentRepository.existsByNameIgnoreCaseAndDeletedFalse(name)) {
            throw new BusinessException("Department name already exists", "DEPARTMENT_NAME_EXISTS");
        }

        String code = resolveCode(request.getCode(), name);
        if (departmentRepository.existsByCodeAndDeletedFalse(code)) {
            throw new BusinessException("Department code already exists", "DEPARTMENT_CODE_EXISTS");
        }

        Department department = Department.builder()
                .name(name)
                .code(code)
                .description(normalizeOptional(request.getDescription()))
                .parent(resolveParent(request.getParentUuid(), null))
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        department = departmentRepository.save(department);
        replaceMembers(department, request.getMembers());
        refreshPersistenceContext();

        log.info("Created department: {}", department.getCode());
        return departmentMapper.toDetails(reloadWithMembers(department.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentDetailsResponse update(UUID uuid, DepartmentUpdateRequest request) {
        Department department = findOrThrow(uuid);
        String name = request.getName().trim();

        if (departmentRepository.existsByNameIgnoreCaseAndDeletedFalseAndIdNot(name, department.getId())) {
            throw new BusinessException("Department name already exists", "DEPARTMENT_NAME_EXISTS");
        }

        department.setName(name);
        department.setDescription(normalizeOptional(request.getDescription()));
        if (request.getStatus() != null) {
            department.setStatus(request.getStatus());
        }

        if (Boolean.TRUE.equals(request.getClearParent())) {
            department.setParent(null);
        } else if (request.getParentUuid() != null) {
            department.setParent(resolveParent(request.getParentUuid(), department.getId()));
        }

        departmentRepository.save(department);

        if (request.getMembers() != null) {
            replaceMembers(department, request.getMembers());
        }

        refreshPersistenceContext();
        log.info("Updated department: {}", department.getCode());
        return departmentMapper.toDetails(reloadWithMembers(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public DepartmentDetailsResponse getByUuid(UUID uuid) {
        return departmentMapper.toDetails(reloadWithMembers(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Department department = findOrThrow(uuid);
        long children = departmentRepository.countByParentIdAndDeletedFalse(department.getId());
        if (children > 0) {
            throw new BusinessException("Cannot delete department that has child departments");
        }

        department.softDelete();
        departmentRepository.save(department);
        departmentMemberRepository.softDeleteByDepartmentId(department.getId());

        log.info("Soft-deleted department: {}", department.getCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<DepartmentListResponse> search(
            String search,
            EntityStatus status,
            String parentUuid,
            Pageable pageable
    ) {
        Page<Department> page = departmentRepository.findAll(
                DepartmentSpecification.filter(search, status, parentUuid),
                pageable
        );

        Page<DepartmentListResponse> mapped = page.map(department -> {
            DepartmentListResponse response = departmentMapper.toList(department);
            response.setMemberCount(
                    (int) departmentMemberRepository.countByDepartmentIdAndDeletedFalse(department.getId())
            );
            return response;
        });
        return PageResponse.from(mapped);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDropdownResponse> dropdown() {
        return departmentRepository.findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(departmentMapper::toDropdown)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentMemberResponse addMember(UUID departmentUuid, DepartmentMemberRequest request) {
        Department department = findOrThrow(departmentUuid);
        User user = resolveUser(request.getUserUuid());
        syncMembers(department, List.of(request), false);
        refreshPersistenceContext();

        DepartmentMember member = departmentMemberRepository
                .findActiveByDepartmentIdAndUserUuid(department.getId(), user.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Department member not found"));
        return departmentMapper.toMemberResponse(member);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMember(UUID departmentUuid, UUID userUuid) {
        Department department = findOrThrow(departmentUuid);
        DepartmentMember member = departmentMemberRepository
                .findActiveByDepartmentIdAndUserUuid(department.getId(), userUuid)
                .orElseThrow(() -> new EntityNotFoundException("Department member not found"));
        member.softDelete();
        departmentMemberRepository.save(member);
        log.info("Removed user {} from department {}", userUuid, department.getCode());
    }

    private void replaceMembers(Department department, List<DepartmentMemberRequest> members) {
        departmentMemberRepository.softDeleteByDepartmentId(department.getId());
        if (members == null || members.isEmpty()) {
            return;
        }
        syncMembers(department, members, true);
    }

    private void syncMembers(
            Department department,
            List<DepartmentMemberRequest> members,
            boolean alreadySoftDeleted
    ) {
        validateUniqueUsers(members);
        if (!alreadySoftDeleted) {
            // keep existing others; only upsert provided users
        }

        Map<Long, DepartmentMember> existingByUserId = departmentMemberRepository
                .findByDepartmentId(department.getId())
                .stream()
                .collect(Collectors.toMap(m -> m.getUser().getId(), m -> m, (a, b) -> a));

        List<DepartmentMember> toSave = new ArrayList<>();
        boolean leadAssigned = false;

        for (DepartmentMemberRequest item : members) {
            User user = resolveUser(item.getUserUuid());
            boolean lead = Boolean.TRUE.equals(item.getLead());
            if (lead) {
                if (leadAssigned) {
                    throw new BusinessException("Only one department lead is allowed");
                }
                leadAssigned = true;
            }

            DepartmentMember existing = existingByUserId.get(user.getId());
            if (existing != null) {
                existing.setDeleted(Boolean.FALSE);
                existing.setStatus(EntityStatus.ACTIVE);
                existing.setDesignation(item.getDesignation().trim());
                existing.setLead(lead);
                toSave.add(existing);
            } else {
                toSave.add(DepartmentMember.builder()
                        .department(department)
                        .user(user)
                        .designation(item.getDesignation().trim())
                        .lead(lead)
                        .status(EntityStatus.ACTIVE)
                        .deleted(Boolean.FALSE)
                        .build());
            }
        }

        if (leadAssigned) {
            Set<Long> leadUserIds = toSave.stream()
                    .filter(m -> Boolean.TRUE.equals(m.getLead()))
                    .map(m -> m.getUser().getId())
                    .collect(Collectors.toSet());
            for (DepartmentMember existing : departmentMemberRepository.findByDepartmentIdAndDeletedFalse(department.getId())) {
                if (Boolean.TRUE.equals(existing.getLead()) && !leadUserIds.contains(existing.getUser().getId())) {
                    existing.setLead(Boolean.FALSE);
                    departmentMemberRepository.save(existing);
                }
            }
        }

        departmentMemberRepository.saveAll(toSave);
    }

    private void validateUniqueUsers(List<DepartmentMemberRequest> members) {
        Set<UUID> seen = new HashSet<>();
        for (DepartmentMemberRequest member : members) {
            if (!seen.add(member.getUserUuid())) {
                throw new BusinessException("Duplicate user in member list");
            }
        }
    }

    private User resolveUser(UUID userUuid) {
        return userRepository.findByUuidAndDeletedFalse(userUuid)
                .filter(user -> user.getStatus() == EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User", userUuid));
    }

    private Department resolveParent(UUID parentUuid, Long currentDepartmentId) {
        if (parentUuid == null) {
            return null;
        }
        Department parent = departmentRepository.findByUuidAndDeletedFalse(parentUuid)
                .orElseThrow(() -> new EntityNotFoundException("Parent department", parentUuid));
        if (parent.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Parent department must be active");
        }
        if (currentDepartmentId != null && parent.getId().equals(currentDepartmentId)) {
            throw new BusinessException("Department cannot be its own parent");
        }
        return parent;
    }

    private String resolveCode(String requestedCode, String name) {
        if (StringUtils.hasText(requestedCode)) {
            return requestedCode.trim().toUpperCase();
        }
        String base = name.trim().toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_|_$", "");
        if (!StringUtils.hasText(base)) {
            base = "DEPT";
        }
        if (base.length() > 40) {
            base = base.substring(0, 40);
        }
        String candidate = base;
        int suffix = 1;
        while (departmentRepository.existsByCodeAndDeletedFalse(candidate)) {
            candidate = base + "_" + suffix++;
        }
        return candidate;
    }

    private Department findOrThrow(UUID uuid) {
        return departmentRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Department", uuid));
    }

    private Department reloadWithMembers(UUID uuid) {
        return departmentRepository.findByUuidWithMembers(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Department", uuid));
    }

    private void refreshPersistenceContext() {
        entityManager.flush();
        entityManager.clear();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
