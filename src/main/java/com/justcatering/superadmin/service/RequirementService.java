package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.RequirementCreateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.RequirementDetailsResponse;
import com.justcatering.superadmin.dto.response.RequirementListResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * On-site requirement management service contract.
 */
public interface RequirementService {

    /**
     * Creates an on-site requirement.
     *
     * @param request create payload
     * @return created requirement details
     */
    RequirementDetailsResponse create(RequirementCreateRequest request);

    /**
     * Returns requirement details by UUID.
     *
     * @param uuid requirement UUID
     * @return requirement details
     */
    RequirementDetailsResponse getByUuid(UUID uuid);

    /**
     * Searches and filters requirements.
     *
     * @param search           free-text search
     * @param clientUuid       client filter
     * @param assignedUserUuid assigned user filter
     * @param pageable         pagination
     * @return page of requirements
     */
    PageResponse<RequirementListResponse> search(
            String search,
            UUID clientUuid,
            UUID assignedUserUuid,
            Pageable pageable
    );

    /**
     * Updates an on-site requirement.
     *
     * @param uuid    requirement UUID
     * @param request update payload
     * @return updated requirement details
     */
    RequirementDetailsResponse update(UUID uuid, RequirementCreateRequest request);

    /**
     * Soft-deletes a requirement by UUID.
     *
     * @param uuid requirement UUID
     */
    void delete(UUID uuid);
}
