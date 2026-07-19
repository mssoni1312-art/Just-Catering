package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ManagerAssignmentCreateRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentSyncRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentUpdateRequest;
import com.justcatering.superadmin.dto.response.ManagerAssignmentDetailsResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentListResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentSyncResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Manager assignment service contract.
 */
public interface ManagerAssignmentService {

    /**
     * Creates a manager assignment.
     *
     * @param request create payload
     * @return created assignment details
     */
    ManagerAssignmentDetailsResponse create(ManagerAssignmentCreateRequest request);

    /**
     * Syncs manager assignments for a client from the assignment screen.
     *
     * @param request sync payload
     * @return synced assignments
     */
    ManagerAssignmentSyncResponse sync(ManagerAssignmentSyncRequest request);

    /**
     * Updates a manager assignment.
     *
     * @param uuid    assignment UUID
     * @param request update payload
     * @return updated assignment details
     */
    ManagerAssignmentDetailsResponse update(UUID uuid, ManagerAssignmentUpdateRequest request);

    /**
     * Returns assignment details by UUID.
     *
     * @param uuid assignment UUID
     * @return assignment details
     */
    ManagerAssignmentDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a manager assignment.
     *
     * @param uuid assignment UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters manager assignments.
     *
     * @param search         free-text search
     * @param clientUuid     client filter
     * @param userUuid       user filter
     * @param departmentUuid department filter
     * @param pageable       pagination
     * @return page of assignments
     */
    PageResponse<ManagerAssignmentListResponse> search(
            String search,
            UUID clientUuid,
            UUID userUuid,
            UUID departmentUuid,
            Pageable pageable
    );
}
