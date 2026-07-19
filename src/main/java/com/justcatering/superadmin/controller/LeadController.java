package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.LeadCreateRequest;
import com.justcatering.superadmin.dto.request.LeadUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.LeadDetailsResponse;
import com.justcatering.superadmin.dto.response.LeadDropdownResponse;
import com.justcatering.superadmin.dto.response.LeadListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import com.justcatering.superadmin.service.LeadService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
 * REST controller for meeting lead management APIs.
 */
@RestController
@RequestMapping(AppConstants.LEAD_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Leads", description = "Meeting lead CRUD, search, filter, and dropdown APIs")
public class LeadController {

    private final LeadService leadService;

    /**
     * Creates a meeting lead.
     *
     * @param request create payload
     * @return created lead
     */
    @PostMapping
    @PreAuthorize("hasAuthority('LEAD_CREATE')")
    @Operation(summary = "Create meeting lead")
    public ResponseEntity<ApiResponse<LeadDetailsResponse>> create(
            @Valid @RequestBody LeadCreateRequest request
    ) {
        LeadDetailsResponse data = leadService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a meeting lead.
     *
     * @param uuid    lead UUID
     * @param request update payload
     * @return updated lead
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('LEAD_UPDATE')")
    @Operation(summary = "Update meeting lead")
    public ResponseEntity<ApiResponse<LeadDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody LeadUpdateRequest request
    ) {
        LeadDetailsResponse data = leadService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns meeting lead details.
     *
     * @param uuid lead UUID
     * @return lead details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    @Operation(summary = "Get meeting lead by UUID")
    public ResponseEntity<ApiResponse<LeadDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        LeadDetailsResponse data = leadService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a meeting lead.
     *
     * @param uuid lead UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('LEAD_DELETE')")
    @Operation(summary = "Delete meeting lead")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        leadService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists meeting leads with optional filters.
     *
     * @return page of leads
     */
    @GetMapping
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    @Operation(summary = "List meeting leads")
    public ResponseEntity<ApiResponse<PageResponse<LeadListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) LeadStage leadStage,
            @RequestParam(required = false) UUID productUuid,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal budgetFrom,
            @RequestParam(required = false) BigDecimal budgetTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<LeadListResponse> data = leadService.search(
                search,
                status,
                leadStage,
                productUuid,
                state,
                city,
                budgetFrom,
                budgetTo,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches meeting leads.
     *
     * @return page of leads
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    @Operation(summary = "Search meeting leads")
    public ResponseEntity<ApiResponse<PageResponse<LeadListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, null, null, null, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters meeting leads.
     *
     * @return page of leads
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    @Operation(summary = "Filter meeting leads")
    public ResponseEntity<ApiResponse<PageResponse<LeadListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) LeadStage leadStage,
            @RequestParam(required = false) UUID productUuid,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal budgetFrom,
            @RequestParam(required = false) BigDecimal budgetTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(
                search, status, leadStage, productUuid, state, city,
                budgetFrom, budgetTo, page, size, sortBy, direction
        );
    }

    /**
     * Returns active meeting leads for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAuthority('LEAD_VIEW')")
    @Operation(summary = "Meeting lead dropdown")
    public ResponseEntity<ApiResponse<List<LeadDropdownResponse>>> dropdown() {
        List<LeadDropdownResponse> data = leadService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
