package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.DepartmentCreateRequest;
import com.justcatering.superadmin.dto.request.DepartmentMemberRequest;
import com.justcatering.superadmin.dto.request.DepartmentUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.DepartmentDetailsResponse;
import com.justcatering.superadmin.dto.response.DepartmentDropdownResponse;
import com.justcatering.superadmin.dto.response.DepartmentListResponse;
import com.justcatering.superadmin.dto.response.DepartmentMemberResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.service.DepartmentService;
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
 * REST controller for department and team-member APIs.
 */
@RestController
@RequestMapping(AppConstants.DEPARTMENT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Departments", description = "Department CRUD, members, search, and dropdown APIs")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Creates a department.
     *
     * @param request create payload
     * @return created department
     */
    @PostMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGE')")
    @Operation(summary = "Create department")
    public ResponseEntity<ApiResponse<DepartmentDetailsResponse>> create(
            @Valid @RequestBody DepartmentCreateRequest request
    ) {
        DepartmentDetailsResponse data = departmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a department.
     *
     * @param uuid    department UUID
     * @param request update payload
     * @return updated department
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGE')")
    @Operation(summary = "Update department")
    public ResponseEntity<ApiResponse<DepartmentDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody DepartmentUpdateRequest request
    ) {
        DepartmentDetailsResponse data = departmentService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns department details.
     *
     * @param uuid department UUID
     * @return department details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    @Operation(summary = "Get department by UUID")
    public ResponseEntity<ApiResponse<DepartmentDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        DepartmentDetailsResponse data = departmentService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a department.
     *
     * @param uuid department UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGE')")
    @Operation(summary = "Delete department")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        departmentService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists departments with optional filters.
     *
     * @return page of departments
     */
    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    @Operation(summary = "List departments")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) String parentUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<DepartmentListResponse> data = departmentService.search(
                search,
                status,
                parentUuid,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches departments and members.
     *
     * @return page of departments
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    @Operation(summary = "Search departments")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters departments.
     *
     * @return page of departments
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    @Operation(summary = "Filter departments")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) String parentUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, status, parentUuid, page, size, sortBy, direction);
    }

    /**
     * Returns active departments for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT_VIEW', 'DEPARTMENT_MANAGE')")
    @Operation(summary = "Department dropdown")
    public ResponseEntity<ApiResponse<List<DepartmentDropdownResponse>>> dropdown() {
        List<DepartmentDropdownResponse> data = departmentService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Adds or updates a department member.
     *
     * @param uuid    department UUID
     * @param request member payload
     * @return member response
     */
    @PostMapping("/{uuid}/members")
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGE')")
    @Operation(summary = "Add department member")
    public ResponseEntity<ApiResponse<DepartmentMemberResponse>> addMember(
            @PathVariable UUID uuid,
            @Valid @RequestBody DepartmentMemberRequest request
    ) {
        DepartmentMemberResponse data = departmentService.addMember(uuid, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Removes a member from a department.
     *
     * @param uuid     department UUID
     * @param userUuid user UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}/members/{userUuid}")
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGE')")
    @Operation(summary = "Remove department member")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable UUID uuid,
            @PathVariable UUID userUuid
    ) {
        departmentService.removeMember(uuid, userUuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }
}
