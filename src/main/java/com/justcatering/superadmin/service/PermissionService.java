package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.PermissionDropdownResponse;
import com.justcatering.superadmin.dto.response.PermissionListResponse;
import com.justcatering.superadmin.dto.response.PermissionModuleResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * Permission query service contract.
 * <p>
 * Permissions are seeded; this module exposes read APIs for RBAC UI.
 * </p>
 */
public interface PermissionService {

    /**
     * Searches and filters permissions with pagination.
     *
     * @param search   free-text search
     * @param module   module filter
     * @param status   status filter
     * @param pageable pagination
     * @return page of permissions
     */
    PageResponse<PermissionListResponse> search(
            String search,
            String module,
            EntityStatus status,
            Pageable pageable
    );

    /**
     * Returns active permissions for dropdowns.
     *
     * @return dropdown options
     */
    List<PermissionDropdownResponse> dropdown();

    /**
     * Returns permissions grouped by module.
     *
     * @return module groups
     */
    List<PermissionModuleResponse> groupByModule();
}
