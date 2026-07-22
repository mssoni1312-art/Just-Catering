package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.ClientQueryCreateRequest;
import com.justcatering.superadmin.dto.request.ClientQueryUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.ClientQueryDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientQueryDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientQueryListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import com.justcatering.superadmin.service.ClientQueryService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
 * REST controller for client query / requirement management APIs.
 */
@RestController
@RequestMapping(AppConstants.QUERY_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Queries", description = "Client query CRUD, search, filter, and dropdown APIs")
public class ClientQueryController {

    private final ClientQueryService clientQueryService;

    /**
     * Creates a client query.
     *
     * @param request create payload
     * @return created client query
     */
    @PostMapping
    @PreAuthorize("hasAuthority('QUERY_MANAGE')")
    @Operation(summary = "Create client query")
    public ResponseEntity<ApiResponse<ClientQueryDetailsResponse>> create(
            @Valid @RequestBody ClientQueryCreateRequest request
    ) {
        ClientQueryDetailsResponse data = clientQueryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a client query.
     *
     * @param uuid    client query UUID
     * @param request update payload
     * @return updated client query
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('QUERY_MANAGE')")
    @Operation(summary = "Update client query")
    public ResponseEntity<ApiResponse<ClientQueryDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody ClientQueryUpdateRequest request
    ) {
        ClientQueryDetailsResponse data = clientQueryService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns client query details.
     *
     * @param uuid client query UUID
     * @return client query details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('QUERY_VIEW')")
    @Operation(summary = "Get client query by UUID")
    public ResponseEntity<ApiResponse<ClientQueryDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        ClientQueryDetailsResponse data = clientQueryService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a client query.
     *
     * @param uuid client query UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('QUERY_MANAGE')")
    @Operation(summary = "Delete client query")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        clientQueryService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists client queries with optional filters.
     *
     * @return page of client queries
     */
    @GetMapping
    @PreAuthorize("hasAuthority('QUERY_VIEW')")
    @Operation(summary = "List client queries")
    public ResponseEntity<ApiResponse<PageResponse<ClientQueryListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) QueryStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID assignedUserUuid,
            @RequestParam(required = false) UUID departmentUuid,
            @RequestParam(required = false) String queryType,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<ClientQueryListResponse> data = clientQueryService.search(
                search,
                status,
                priority,
                clientUuid,
                assignedUserUuid,
                departmentUuid,
                queryType,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches client queries.
     *
     * @return page of client queries
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('QUERY_VIEW')")
    @Operation(summary = "Search client queries")
    public ResponseEntity<ApiResponse<PageResponse<ClientQueryListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, null, null, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters client queries.
     *
     * @return page of client queries
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('QUERY_VIEW')")
    @Operation(summary = "Filter client queries")
    public ResponseEntity<ApiResponse<PageResponse<ClientQueryListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) QueryStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID assignedUserUuid,
            @RequestParam(required = false) UUID departmentUuid,
            @RequestParam(required = false) String queryType,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(
                search, status, priority, clientUuid, assignedUserUuid,
                departmentUuid, queryType, page, size, sortBy, direction
        );
    }

    /**
     * Returns active client queries for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('QUERY_VIEW', 'QUERY_MANAGE')")
    @Operation(summary = "Client query dropdown")
    public ResponseEntity<ApiResponse<List<ClientQueryDropdownResponse>>> dropdown() {
        List<ClientQueryDropdownResponse> data = clientQueryService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
