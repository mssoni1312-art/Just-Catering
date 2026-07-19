package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ClientCreateRequest;
import com.justcatering.superadmin.dto.request.ClientUpdateRequest;
import com.justcatering.superadmin.dto.response.ClientDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Client management service contract.
 */
public interface ClientService {

    /**
     * Creates a client.
     *
     * @param request create payload
     * @return created client details
     */
    ClientDetailsResponse create(ClientCreateRequest request);

    /**
     * Updates a client.
     *
     * @param uuid    client UUID
     * @param request update payload
     * @return updated client details
     */
    ClientDetailsResponse update(UUID uuid, ClientUpdateRequest request);

    /**
     * Returns client details by UUID.
     *
     * @param uuid client UUID
     * @return client details
     */
    ClientDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a client.
     *
     * @param uuid client UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters clients.
     *
     * @param search      free-text search
     * @param status      entity status
     * @param clientStage stage filter
     * @param priority    priority filter
     * @param productUuid product filter
     * @param clientType  type filter
     * @param dealFrom    deal date from
     * @param dealTo      deal date to
     * @param pageable    pagination
     * @return page of clients
     */
    PageResponse<ClientListResponse> search(
            String search,
            EntityStatus status,
            ClientStage clientStage,
            Priority priority,
            UUID productUuid,
            String clientType,
            LocalDate dealFrom,
            LocalDate dealTo,
            Pageable pageable
    );

    /**
     * Returns active clients for dropdowns.
     *
     * @return dropdown options
     */
    List<ClientDropdownResponse> dropdown();
}
