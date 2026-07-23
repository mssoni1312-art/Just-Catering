package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.OnSiteTaskPlanPrepareRequest;
import com.justcatering.superadmin.dto.request.OnSiteTaskPlanSaveRequest;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanDetailsResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanListResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanPrepareResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Service contract for on-site manager task plans.
 */
public interface OnSiteTaskPlanService {

    /**
     * Prepares day slots for the next screen from the Create Days Task Plan modal.
     *
     * @param request assign-to member and day count
     * @return day slots and assigned user for the form
     */
    OnSiteTaskPlanPrepareResponse prepare(OnSiteTaskPlanPrepareRequest request);

    /**
     * Saves a completed on-site manager task plan.
     *
     * @param request save payload
     * @return saved plan details
     */
    OnSiteTaskPlanDetailsResponse create(OnSiteTaskPlanSaveRequest request);

    /**
     * Updates an existing task plan.
     *
     * @param uuid    plan UUID
     * @param request save payload
     * @return updated plan details
     */
    OnSiteTaskPlanDetailsResponse update(UUID uuid, OnSiteTaskPlanSaveRequest request);

    /**
     * Returns task plan details.
     *
     * @param uuid plan UUID
     * @return plan details
     */
    OnSiteTaskPlanDetailsResponse getByUuid(UUID uuid);

    /**
     * Lists task plans with optional filters.
     *
     * @param search      free-text search
     * @param clientUuid  client filter
     * @param managerUuid manager filter
     * @param pageable    pagination
     * @return page of plans
     */
    PageResponse<OnSiteTaskPlanListResponse> search(
            String search,
            UUID clientUuid,
            UUID managerUuid,
            Pageable pageable
    );

    /**
     * Soft-deletes a task plan.
     *
     * @param uuid plan UUID
     */
    void delete(UUID uuid);
}
