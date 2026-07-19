package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.ClientDeadlineCreateRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentCreateRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentSyncRequest;
import com.justcatering.superadmin.dto.request.ManagerAssignmentUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.ClientDeadlineDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDeadlineListResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentDetailsResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentListResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentSyncResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.ProjectProfitLossResponse;
import com.justcatering.superadmin.service.ClientDeadlineService;
import com.justcatering.superadmin.service.ManagerAssignmentService;
import com.justcatering.superadmin.service.ProjectProfitLossService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
 * REST controller for project operations: manager assignments, deadlines, and P&amp;L.
 */
@RestController
@RequestMapping(AppConstants.PROJECT_OPS_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Project Ops", description = "Manager assignments, deadline management, and project P&L APIs")
public class ProjectOpsController {

    private final ManagerAssignmentService managerAssignmentService;
    private final ClientDeadlineService clientDeadlineService;
    private final ProjectProfitLossService projectProfitLossService;

    /**
     * Syncs manager assignments for a client from the assignment screen.
     *
     * @param request sync payload with project details and selected members
     * @return synced assignments
     */
    @PostMapping("/assignments")
    @PreAuthorize("hasAnyAuthority('CLIENT_UPDATE', 'DEPARTMENT_MANAGE')")
    @Operation(summary = "Sync manager assignments for a client")
    public ResponseEntity<ApiResponse<ManagerAssignmentSyncResponse>> syncAssignments(
            @Valid @RequestBody ManagerAssignmentSyncRequest request
    ) {
        ManagerAssignmentSyncResponse data = managerAssignmentService.sync(request);
        return ResponseEntity.ok(
                ApiResponse.success("Assignment updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Creates a single manager assignment.
     *
     * @param request create payload
     * @return created assignment
     */
    @PostMapping("/assignments/single")
    @PreAuthorize("hasAnyAuthority('CLIENT_UPDATE', 'DEPARTMENT_MANAGE')")
    @Operation(summary = "Create a single manager assignment")
    public ResponseEntity<ApiResponse<ManagerAssignmentDetailsResponse>> createAssignment(
            @Valid @RequestBody ManagerAssignmentCreateRequest request
    ) {
        ManagerAssignmentDetailsResponse data = managerAssignmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a manager assignment.
     *
     * @param uuid    assignment UUID
     * @param request update payload
     * @return updated assignment
     */
    @PutMapping("/assignments/{uuid}")
    @PreAuthorize("hasAnyAuthority('CLIENT_UPDATE', 'DEPARTMENT_MANAGE')")
    @Operation(summary = "Update manager assignment")
    public ResponseEntity<ApiResponse<ManagerAssignmentDetailsResponse>> updateAssignment(
            @PathVariable UUID uuid,
            @Valid @RequestBody ManagerAssignmentUpdateRequest request
    ) {
        ManagerAssignmentDetailsResponse data = managerAssignmentService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns manager assignment details.
     *
     * @param uuid assignment UUID
     * @return assignment details
     */
    @GetMapping("/assignments/{uuid}")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "Get manager assignment by UUID")
    public ResponseEntity<ApiResponse<ManagerAssignmentDetailsResponse>> getAssignment(@PathVariable UUID uuid) {
        ManagerAssignmentDetailsResponse data = managerAssignmentService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a manager assignment.
     *
     * @param uuid assignment UUID
     * @return success response
     */
    @DeleteMapping("/assignments/{uuid}")
    @PreAuthorize("hasAnyAuthority('CLIENT_UPDATE', 'DEPARTMENT_MANAGE')")
    @Operation(summary = "Delete manager assignment")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable UUID uuid) {
        managerAssignmentService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists manager assignments with optional filters.
     *
     * @return page of assignments
     */
    @GetMapping("/assignments")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "List manager assignments")
    public ResponseEntity<ApiResponse<PageResponse<ManagerAssignmentListResponse>>> listAssignments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID userUuid,
            @RequestParam(required = false) UUID departmentUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<ManagerAssignmentListResponse> data = managerAssignmentService.search(
                search, clientUuid, userUuid, departmentUuid,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Filters manager assignments.
     *
     * @return page of assignments
     */
    @GetMapping("/assignments/filter")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "Filter manager assignments")
    public ResponseEntity<ApiResponse<PageResponse<ManagerAssignmentListResponse>>> filterAssignments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID userUuid,
            @RequestParam(required = false) UUID departmentUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return listAssignments(search, clientUuid, userUuid, departmentUuid, page, size, sortBy, direction);
    }

    /**
     * Records a client deadline change.
     *
     * @param request create payload
     * @return created deadline record
     */
    @PostMapping("/deadlines")
    @PreAuthorize("hasAnyAuthority('CLIENT_UPDATE', 'DEPARTMENT_MANAGE')")
    @Operation(summary = "Record client deadline change")
    public ResponseEntity<ApiResponse<ClientDeadlineDetailsResponse>> createDeadline(
            @Valid @RequestBody ClientDeadlineCreateRequest request
    ) {
        ClientDeadlineDetailsResponse data = clientDeadlineService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Returns client deadline details.
     *
     * @param uuid deadline UUID
     * @return deadline details
     */
    @GetMapping("/deadlines/{uuid}")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "Get client deadline by UUID")
    public ResponseEntity<ApiResponse<ClientDeadlineDetailsResponse>> getDeadline(@PathVariable UUID uuid) {
        ClientDeadlineDetailsResponse data = clientDeadlineService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Lists client deadline records with optional filters.
     *
     * @return page of deadline records
     */
    @GetMapping("/deadlines")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "List client deadlines")
    public ResponseEntity<ApiResponse<PageResponse<ClientDeadlineListResponse>>> listDeadlines(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID departmentUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<ClientDeadlineListResponse> data = clientDeadlineService.search(
                search, clientUuid, departmentUuid,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Filters client deadline records.
     *
     * @return page of deadline records
     */
    @GetMapping("/deadlines/filter")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "Filter client deadlines")
    public ResponseEntity<ApiResponse<PageResponse<ClientDeadlineListResponse>>> filterDeadlines(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID departmentUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return listDeadlines(search, clientUuid, departmentUuid, page, size, sortBy, direction);
    }

    /**
     * Returns computed project profit and loss for a client.
     *
     * @param clientUuid client UUID
     * @return P&amp;L summary
     */
    @GetMapping("/clients/{clientUuid}/pnl")
    @PreAuthorize("hasAnyAuthority('CLIENT_VIEW', 'DASHBOARD_VIEW')")
    @Operation(summary = "Get client project P&L")
    public ResponseEntity<ApiResponse<ProjectProfitLossResponse>> getProfitLoss(@PathVariable UUID clientUuid) {
        ProjectProfitLossResponse data = projectProfitLossService.getProfitLoss(clientUuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
