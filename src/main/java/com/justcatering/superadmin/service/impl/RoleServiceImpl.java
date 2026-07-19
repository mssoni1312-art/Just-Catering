package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.RoleCreateRequest;
import com.justcatering.superadmin.dto.request.RoleUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.RoleDetailsResponse;
import com.justcatering.superadmin.dto.response.RoleDropdownResponse;
import com.justcatering.superadmin.dto.response.RoleListResponse;
import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.entity.RolePermission;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.RoleMapper;
import com.justcatering.superadmin.repository.PermissionRepository;
import com.justcatering.superadmin.repository.RolePermissionRepository;
import com.justcatering.superadmin.repository.RoleRepository;
import com.justcatering.superadmin.repository.UserRoleEntityRepository;
import com.justcatering.superadmin.service.RoleService;
import com.justcatering.superadmin.specification.RoleSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
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

/**
 * Default implementation of {@link RoleService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final RoleMapper roleMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDetailsResponse create(RoleCreateRequest request) {
        String code = request.getCode().trim().toUpperCase();
        if (roleRepository.existsByCodeAndDeletedFalse(code)) {
            throw new BusinessException("Role code already exists", "ROLE_CODE_EXISTS");
        }
        if (roleRepository.existsByNameIgnoreCaseAndDeletedFalse(request.getName().trim())) {
            throw new BusinessException("Role name already exists", "ROLE_NAME_EXISTS");
        }

        EntityStatus status = request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE;
        Role role = Role.builder()
                .name(request.getName().trim())
                .code(code)
                .description(normalizeOptional(request.getDescription()))
                .system(Boolean.FALSE)
                .status(status)
                .deleted(Boolean.FALSE)
                .build();

        role = roleRepository.save(role);
        assignPermissions(role, resolvePermissions(request.getPermissionUuids()));
        refreshPersistenceContext();

        log.info("Created role: {}", role.getCode());
        return roleMapper.toDetails(reloadWithPermissions(role.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDetailsResponse update(UUID uuid, RoleUpdateRequest request) {
        Role role = findRoleOrThrow(uuid);

        if (roleRepository.existsByNameIgnoreCaseAndDeletedFalseAndIdNot(request.getName().trim(), role.getId())) {
            throw new BusinessException("Role name already exists", "ROLE_NAME_EXISTS");
        }

        role.setName(request.getName().trim());
        role.setDescription(normalizeOptional(request.getDescription()));
        if (request.getStatus() != null) {
            if (Boolean.TRUE.equals(role.getSystem()) && request.getStatus() != EntityStatus.ACTIVE) {
                throw new BusinessException("System roles cannot be deactivated");
            }
            role.setStatus(request.getStatus());
        }

        roleRepository.save(role);
        replacePermissions(role, resolvePermissions(request.getPermissionUuids()));
        refreshPersistenceContext();

        log.info("Updated role: {}", role.getCode());
        return roleMapper.toDetails(reloadWithPermissions(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public RoleDetailsResponse getByUuid(UUID uuid) {
        return roleMapper.toDetails(reloadWithPermissions(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Role role = findRoleOrThrow(uuid);
        if (Boolean.TRUE.equals(role.getSystem())) {
            throw new BusinessException("System roles cannot be deleted");
        }
        long assignedUsers = userRoleEntityRepository.countByRoleIdAndDeletedFalse(role.getId());
        if (assignedUsers > 0) {
            throw new BusinessException("Cannot delete role assigned to active users");
        }

        role.softDelete();
        roleRepository.save(role);
        rolePermissionRepository.softDeleteByRoleId(role.getId());

        log.info("Soft-deleted role: {}", role.getCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleListResponse> search(
            String search,
            EntityStatus status,
            Boolean system,
            Pageable pageable
    ) {
        Page<Role> page = roleRepository.findAll(
                RoleSpecification.filter(search, status, system),
                pageable
        );
        return PageResponse.from(page.map(roleMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<RoleDropdownResponse> dropdown() {
        return roleRepository.findByDeletedFalseAndStatusOrderByNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(roleMapper::toDropdown)
                .toList();
    }

    private void assignPermissions(Role role, Set<Permission> permissions) {
        if (permissions.isEmpty()) {
            return;
        }
        Map<Long, RolePermission> existingByPermissionId = rolePermissionRepository.findByRoleId(role.getId())
                .stream()
                .collect(Collectors.toMap(link -> link.getPermission().getId(), link -> link, (a, b) -> a));

        List<RolePermission> toSave = new ArrayList<>();
        for (Permission permission : permissions) {
            RolePermission existing = existingByPermissionId.get(permission.getId());
            if (existing != null) {
                existing.setDeleted(Boolean.FALSE);
                existing.setStatus(EntityStatus.ACTIVE);
                toSave.add(existing);
            } else {
                toSave.add(RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .status(EntityStatus.ACTIVE)
                        .deleted(Boolean.FALSE)
                        .build());
            }
        }
        rolePermissionRepository.saveAll(toSave);
    }

    private void replacePermissions(Role role, Set<Permission> permissions) {
        rolePermissionRepository.softDeleteByRoleId(role.getId());
        assignPermissions(role, permissions);
    }

    private Set<Permission> resolvePermissions(Set<UUID> permissionUuids) {
        if (permissionUuids == null || permissionUuids.isEmpty()) {
            return Collections.emptySet();
        }
        List<Permission> permissions = permissionRepository.findByUuidInAndDeletedFalse(permissionUuids);
        if (permissions.size() != permissionUuids.size()) {
            throw new EntityNotFoundException("One or more permissions were not found");
        }
        return new HashSet<>(permissions);
    }

    private Role findRoleOrThrow(UUID uuid) {
        return roleRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Role", uuid));
    }

    private Role reloadWithPermissions(UUID uuid) {
        return roleRepository.findByUuidWithPermissions(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Role", uuid));
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
