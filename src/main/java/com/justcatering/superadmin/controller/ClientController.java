package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.ClientCreateRequest;
import com.justcatering.superadmin.dto.request.ClientUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.ClientDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientListResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.service.ClientService;
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
 * REST controller for client management APIs.
 */
@RestController
@RequestMapping(AppConstants.CLIENT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clients", description = "Client CRUD, search, filter, and dropdown APIs")
public class ClientController {

    private final ClientService clientService;

    /**
     * Creates a client.
     *
     * @param request create payload
     * @return created client
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT_CREATE')")
    @Operation(summary = "Create client")
    public ResponseEntity<ApiResponse<ClientDetailsResponse>> create(
            @Valid @RequestBody ClientCreateRequest request
    ) {
        ClientDetailsResponse data = clientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a client.
     *
     * @param uuid    client UUID
     * @param request update payload
     * @return updated client
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('CLIENT_UPDATE')")
    @Operation(summary = "Update client")
    public ResponseEntity<ApiResponse<ClientDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody ClientUpdateRequest request
    ) {
        ClientDetailsResponse data = clientService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns client details.
     *
     * @param uuid client UUID
     * @return client details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('CLIENT_VIEW')")
    @Operation(summary = "Get client by UUID")
    public ResponseEntity<ApiResponse<ClientDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        ClientDetailsResponse data = clientService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a client.
     *
     * @param uuid client UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    @Operation(summary = "Delete client")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        clientService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists clients with optional filters.
     *
     * @return page of clients
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CLIENT_VIEW')")
    @Operation(summary = "List clients")
    public ResponseEntity<ApiResponse<PageResponse<ClientListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) ClientStage clientStage,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) UUID productUuid,
            @RequestParam(required = false) String clientType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dealFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dealTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<ClientListResponse> data = clientService.search(
                search,
                status,
                clientStage,
                priority,
                productUuid,
                clientType,
                dealFrom,
                dealTo,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches clients.
     *
     * @return page of clients
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('CLIENT_VIEW')")
    @Operation(summary = "Search clients")
    public ResponseEntity<ApiResponse<PageResponse<ClientListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, null, null, null, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters clients.
     *
     * @return page of clients
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('CLIENT_VIEW')")
    @Operation(summary = "Filter clients")
    public ResponseEntity<ApiResponse<PageResponse<ClientListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) ClientStage clientStage,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) UUID productUuid,
            @RequestParam(required = false) String clientType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dealFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dealTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(
                search, status, clientStage, priority, productUuid, clientType,
                dealFrom, dealTo, page, size, sortBy, direction
        );
    }

    /**
     * Returns active clients for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'FOLLOWUP_MANAGE', 'PAYMENT_MANAGE', 'QUERY_MANAGE')")
    @Operation(summary = "Client dropdown")
    public ResponseEntity<ApiResponse<List<ClientDropdownResponse>>> dropdown() {
        List<ClientDropdownResponse> data = clientService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
