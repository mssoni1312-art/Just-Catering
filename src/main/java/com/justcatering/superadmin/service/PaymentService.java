package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.PaymentCreateRequest;
import com.justcatering.superadmin.dto.request.PaymentUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.PaymentDetailsResponse;
import com.justcatering.superadmin.dto.response.PaymentDropdownResponse;
import com.justcatering.superadmin.dto.response.PaymentListResponse;
import com.justcatering.superadmin.dto.response.PaymentOverviewResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.PaymentMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Payment management service contract.
 */
public interface PaymentService {

    /**
     * Creates a payment receipt.
     *
     * @param request create payload
     * @return created payment details
     */
    PaymentDetailsResponse create(PaymentCreateRequest request);

    /**
     * Updates a payment receipt.
     *
     * @param uuid    payment UUID
     * @param request update payload
     * @return updated payment details
     */
    PaymentDetailsResponse update(UUID uuid, PaymentUpdateRequest request);

    /**
     * Returns payment details by UUID.
     *
     * @param uuid payment UUID
     * @return payment details
     */
    PaymentDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a payment receipt.
     *
     * @param uuid payment UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters payments.
     *
     * @param search      free-text search
     * @param status      entity status
     * @param paymentMode payment mode
     * @param clientUuid  client filter
     * @param productUuid product filter
     * @param paymentFrom payment date from
     * @param paymentTo   payment date to
     * @param pageable    pagination
     * @return page of payments
     */
    PageResponse<PaymentListResponse> search(
            String search,
            EntityStatus status,
            PaymentMode paymentMode,
            UUID clientUuid,
            UUID productUuid,
            LocalDate paymentFrom,
            LocalDate paymentTo,
            Pageable pageable
    );

    /**
     * Returns payment overview for a client.
     *
     * @param clientUuid client UUID
     * @return overview with balance and recent receipts
     */
    PaymentOverviewResponse getClientOverview(UUID clientUuid);

    /**
     * Returns active payments for dropdowns.
     *
     * @return dropdown options
     */
    List<PaymentDropdownResponse> dropdown();
}
