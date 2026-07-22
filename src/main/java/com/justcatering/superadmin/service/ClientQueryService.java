package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ClientQueryCreateRequest;
import com.justcatering.superadmin.dto.request.ClientQueryUpdateRequest;
import com.justcatering.superadmin.dto.response.ClientQueryDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientQueryDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientQueryListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Client query management service contract.
 */
public interface ClientQueryService {

    /**
     * Creates a client query.
     *
     * @param request create payload
     * @return created client query details
     */
    ClientQueryDetailsResponse create(ClientQueryCreateRequest request);

    /**
     * Updates a client query.
     *
     * @param uuid    client query UUID
     * @param request update payload
     * @return updated client query details
     */
    ClientQueryDetailsResponse update(UUID uuid, ClientQueryUpdateRequest request);

    /**
     * Returns client query details by UUID.
     *
     * @param uuid client query UUID
     * @return client query details
     */
    ClientQueryDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a client query.
     *
     * @param uuid client query UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters client queries.
     *
     * @param search           free-text search
     * @param queryStatus      workflow status filter
     * @param priority         priority filter
     * @param clientUuid       client filter
     * @param assignedUserUuid assigned user filter
     * @param departmentUuid   department filter
     * @param pageable         pagination
     * @return page of client queries
     */
    PageResponse<ClientQueryListResponse> search(
            String search,
            QueryStatus queryStatus,
            Priority priority,
            UUID clientUuid,
            UUID assignedUserUuid,
            UUID departmentUuid,
            String queryType,
            Pageable pageable
    );

    /**
     * Returns active client queries for dropdowns.
     *
     * @return dropdown options
     */
    List<ClientQueryDropdownResponse> dropdown();
}
