package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.RoleCreateRequest;
import com.justcatering.superadmin.dto.request.RoleUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.RoleDetailsResponse;
import com.justcatering.superadmin.dto.response.RoleDropdownResponse;
import com.justcatering.superadmin.dto.response.RoleListResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Role management service contract.
 */
public interface RoleService {

    /**
     * Creates a new role.
     *
     * @param request create payload
     * @return created role details
     */
    RoleDetailsResponse create(RoleCreateRequest request);

    /**
     * Updates an existing role.
     *
     * @param uuid    role UUID
     * @param request update payload
     * @return updated role details
     */
    RoleDetailsResponse update(UUID uuid, RoleUpdateRequest request);

    /**
     * Returns role details by UUID.
     *
     * @param uuid role UUID
     * @return role details
     */
    RoleDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a non-system role.
     *
     * @param uuid role UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters roles with pagination.
     *
     * @param search   free-text search
     * @param status   status filter
     * @param system   system-role filter
     * @param pageable pagination
     * @return page of roles
     */
    PageResponse<RoleListResponse> search(
            String search,
            EntityStatus status,
            Boolean system,
            Pageable pageable
    );

    /**
     * Returns active roles for dropdowns.
     *
     * @return dropdown options
     */
    List<RoleDropdownResponse> dropdown();
}
