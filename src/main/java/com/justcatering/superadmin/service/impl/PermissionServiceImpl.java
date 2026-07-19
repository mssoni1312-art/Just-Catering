package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.PermissionDropdownResponse;
import com.justcatering.superadmin.dto.response.PermissionListResponse;
import com.justcatering.superadmin.dto.response.PermissionModuleResponse;
import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.mapper.PermissionMapper;
import com.justcatering.superadmin.repository.PermissionRepository;
import com.justcatering.superadmin.service.PermissionService;
import com.justcatering.superadmin.specification.PermissionSpecification;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link PermissionService}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<PermissionListResponse> search(
            String search,
            String module,
            EntityStatus status,
            Pageable pageable
    ) {
        Page<Permission> page = permissionRepository.findAll(
                PermissionSpecification.filter(search, module, status),
                pageable
        );
        return PageResponse.from(page.map(permissionMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PermissionDropdownResponse> dropdown() {
        return permissionRepository
                .findByDeletedFalseAndStatusOrderByModuleAscNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(permissionMapper::toDropdown)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PermissionModuleResponse> groupByModule() {
        List<Permission> permissions = permissionRepository
                .findByDeletedFalseAndStatusOrderByModuleAscNameAsc(EntityStatus.ACTIVE);

        Map<String, List<PermissionDropdownResponse>> grouped = new LinkedHashMap<>();
        for (Permission permission : permissions) {
            grouped.computeIfAbsent(permission.getModule(), key -> new ArrayList<>())
                    .add(permissionMapper.toDropdown(permission));
        }

        return grouped.entrySet().stream()
                .map(entry -> PermissionModuleResponse.builder()
                        .module(entry.getKey())
                        .permissions(entry.getValue())
                        .build())
                .toList();
    }
}
