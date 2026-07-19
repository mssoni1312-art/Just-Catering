package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.PaymentCreateRequest;
import com.justcatering.superadmin.dto.request.PaymentUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.PaymentDetailsResponse;
import com.justcatering.superadmin.dto.response.PaymentDropdownResponse;
import com.justcatering.superadmin.dto.response.PaymentListResponse;
import com.justcatering.superadmin.dto.response.PaymentOverviewResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.PaymentMode;
import com.justcatering.superadmin.service.PaymentService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for payment / receipt management APIs.
 */
@RestController
@RequestMapping(AppConstants.PAYMENT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Payments", description = "Payment CRUD, search, filter, overview, and dropdown APIs")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates a payment receipt.
     *
     * @param request create payload
     * @return created payment
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PAYMENT_MANAGE')")
    @Operation(summary = "Create payment receipt")
    public ResponseEntity<ApiResponse<PaymentDetailsResponse>> create(
            @Valid @RequestBody PaymentCreateRequest request
    ) {
        PaymentDetailsResponse data = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a payment receipt.
     *
     * @param uuid    payment UUID
     * @param request update payload
     * @return updated payment
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('PAYMENT_MANAGE')")
    @Operation(summary = "Update payment receipt")
    public ResponseEntity<ApiResponse<PaymentDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody PaymentUpdateRequest request
    ) {
        PaymentDetailsResponse data = paymentService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns payment details.
     *
     * @param uuid payment UUID
     * @return payment details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "Get payment by UUID")
    public ResponseEntity<ApiResponse<PaymentDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        PaymentDetailsResponse data = paymentService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a payment receipt.
     *
     * @param uuid payment UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('PAYMENT_MANAGE')")
    @Operation(summary = "Delete payment receipt")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        paymentService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists payments with optional filters.
     *
     * @return page of payments
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "List payments")
    public ResponseEntity<ApiResponse<PageResponse<PaymentListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) PaymentMode paymentMode,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID productUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<PaymentListResponse> data = paymentService.search(
                search,
                status,
                paymentMode,
                clientUuid,
                productUuid,
                paymentFrom,
                paymentTo,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches payments.
     *
     * @return page of payments
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "Search payments")
    public ResponseEntity<ApiResponse<PageResponse<PaymentListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, null, null, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters payments.
     *
     * @return page of payments
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "Filter payments")
    public ResponseEntity<ApiResponse<PageResponse<PaymentListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) PaymentMode paymentMode,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID productUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(
                search, status, paymentMode, clientUuid, productUuid,
                paymentFrom, paymentTo, page, size, sortBy, direction
        );
    }

    /**
     * Returns payment overview for a client.
     *
     * @param clientUuid client UUID
     * @return overview with balance and recent receipts
     */
    @GetMapping("/client/{clientUuid}/overview")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "Get client payment overview")
    public ResponseEntity<ApiResponse<PaymentOverviewResponse>> getClientOverview(
            @PathVariable UUID clientUuid
    ) {
        PaymentOverviewResponse data = paymentService.getClientOverview(clientUuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns active payments for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('PAYMENT_VIEW', 'PAYMENT_MANAGE')")
    @Operation(summary = "Payment dropdown")
    public ResponseEntity<ApiResponse<List<PaymentDropdownResponse>>> dropdown() {
        List<PaymentDropdownResponse> data = paymentService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
