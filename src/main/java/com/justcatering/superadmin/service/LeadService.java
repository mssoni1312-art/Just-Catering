package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.LeadCreateRequest;
import com.justcatering.superadmin.dto.request.LeadUpdateRequest;
import com.justcatering.superadmin.dto.response.LeadDetailsResponse;
import com.justcatering.superadmin.dto.response.LeadDropdownResponse;
import com.justcatering.superadmin.dto.response.LeadListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Meeting lead management service contract.
 */
public interface LeadService {

    /**
     * Creates a meeting lead.
     *
     * @param request create payload
     * @return created lead details
     */
    LeadDetailsResponse create(LeadCreateRequest request);

    /**
     * Updates a meeting lead.
     *
     * @param uuid    lead UUID
     * @param request update payload
     * @return updated lead details
     */
    LeadDetailsResponse update(UUID uuid, LeadUpdateRequest request);

    /**
     * Returns lead details by UUID.
     *
     * @param uuid lead UUID
     * @return lead details
     */
    LeadDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a meeting lead.
     *
     * @param uuid lead UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters meeting leads.
     *
     * @param search      free-text search
     * @param status      entity status
     * @param leadStage   stage filter
     * @param productUuid product filter
     * @param state       state filter
     * @param city        city filter
     * @param budgetFrom  minimum approximate budget
     * @param budgetTo    maximum approximate budget
     * @param pageable    pagination
     * @return page of leads
     */
    PageResponse<LeadListResponse> search(
            String search,
            EntityStatus status,
            LeadStage leadStage,
            UUID productUuid,
            String state,
            String city,
            BigDecimal budgetFrom,
            BigDecimal budgetTo,
            Pageable pageable
    );

    /**
     * Returns active leads for dropdowns.
     *
     * @return dropdown options
     */
    List<LeadDropdownResponse> dropdown();
}
