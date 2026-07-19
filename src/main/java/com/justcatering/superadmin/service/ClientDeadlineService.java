package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ClientDeadlineCreateRequest;
import com.justcatering.superadmin.dto.response.ClientDeadlineDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDeadlineListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Client deadline service contract.
 */
public interface ClientDeadlineService {

    /**
     * Records a client deadline change.
     *
     * @param request create payload
     * @return created deadline details
     */
    ClientDeadlineDetailsResponse create(ClientDeadlineCreateRequest request);

    /**
     * Returns deadline details by UUID.
     *
     * @param uuid deadline UUID
     * @return deadline details
     */
    ClientDeadlineDetailsResponse getByUuid(UUID uuid);

    /**
     * Searches and filters client deadlines.
     *
     * @param search         free-text search
     * @param clientUuid     client filter
     * @param departmentUuid department filter
     * @param pageable       pagination
     * @return page of deadline records
     */
    PageResponse<ClientDeadlineListResponse> search(
            String search,
            UUID clientUuid,
            UUID departmentUuid,
            Pageable pageable
    );
}
