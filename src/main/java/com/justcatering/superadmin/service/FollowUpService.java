package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.FollowUpCreateRequest;
import com.justcatering.superadmin.dto.request.FollowUpUpdateRequest;
import com.justcatering.superadmin.dto.response.FollowUpDetailsResponse;
import com.justcatering.superadmin.dto.response.FollowUpDropdownResponse;
import com.justcatering.superadmin.dto.response.FollowUpListResponse;
import com.justcatering.superadmin.dto.response.FollowUpReminderOptionResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Follow-up management service contract.
 */
public interface FollowUpService {

    /**
     * Creates a follow-up.
     *
     * @param request create payload
     * @return created follow-up details
     */
    FollowUpDetailsResponse create(FollowUpCreateRequest request);

    /**
     * Updates a follow-up.
     *
     * @param uuid    follow-up UUID
     * @param request update payload
     * @return updated follow-up details
     */
    FollowUpDetailsResponse update(UUID uuid, FollowUpUpdateRequest request);

    /**
     * Returns follow-up details by UUID.
     *
     * @param uuid follow-up UUID
     * @return follow-up details
     */
    FollowUpDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a follow-up.
     *
     * @param uuid follow-up UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters follow-ups.
     *
     * @param search           free-text search
     * @param status           follow-up outcome status
     * @param type             follow-up type
     * @param clientUuid       client-only filter
     * @param leadUuid         lead-only filter
     * @param assignedUserUuid assigned user filter
     * @param followUpFrom     follow-up date from
     * @param followUpTo       follow-up date to
     * @param pageable         pagination
     * @return page of follow-ups
     */
    PageResponse<FollowUpListResponse> search(
            String search,
            FollowUpStatus status,
            FollowUpType type,
            UUID clientUuid,
            UUID leadUuid,
            UUID assignedUserUuid,
            LocalDate followUpFrom,
            LocalDate followUpTo,
            Pageable pageable
    );

    /**
     * Returns active follow-ups for dropdowns.
     *
     * @return dropdown options
     */
    List<FollowUpDropdownResponse> dropdown();

    /**
     * Returns reminder preset options for the follow-up form.
     *
     * @return reminder options
     */
    List<FollowUpReminderOptionResponse> reminderOptions();
}
