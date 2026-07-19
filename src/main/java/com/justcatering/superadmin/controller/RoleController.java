package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.RoleCreateRequest;
import com.justcatering.superadmin.dto.request.RoleUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.RoleDetailsResponse;
import com.justcatering.superadmin.dto.response.RoleDropdownResponse;
import com.justcatering.superadmin.dto.response.RoleListResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.service.RoleService;
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
 * REST controller for role management APIs.
 */
@RestController
@RequestMapping(AppConstants.ROLE_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Roles", description = "Role CRUD, search, filter, and dropdown APIs")
public class RoleController {

    private final RoleService roleService;

    /**
     * Creates a new role.
     *
     * @param request create payload
     * @return created role
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Create role")
    public ResponseEntity<ApiResponse<RoleDetailsResponse>> create(
            @Valid @RequestBody RoleCreateRequest request
    ) {
        RoleDetailsResponse data = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates an existing role.
     *
     * @param uuid    role UUID
     * @param request update payload
     * @return updated role
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Update role")
    public ResponseEntity<ApiResponse<RoleDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        RoleDetailsResponse data = roleService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns role details.
     *
     * @param uuid role UUID
     * @return role details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Get role by UUID")
    public ResponseEntity<ApiResponse<RoleDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        RoleDetailsResponse data = roleService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a non-system role.
     *
     * @param uuid role UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Delete role")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        roleService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists roles with optional filters.
     *
     * @return page of roles
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "List roles")
    public ResponseEntity<ApiResponse<PageResponse<RoleListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) Boolean system,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<RoleListResponse> data = roleService.search(
                search,
                status,
                system,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches roles.
     *
     * @return page of roles
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Search roles")
    public ResponseEntity<ApiResponse<PageResponse<RoleListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters roles.
     *
     * @return page of roles
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Filter roles")
    public ResponseEntity<ApiResponse<PageResponse<RoleListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) Boolean system,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, status, system, page, size, sortBy, direction);
    }

    /**
     * Returns active roles for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW', 'USER_CREATE', 'USER_UPDATE')")
    @Operation(summary = "Role dropdown")
    public ResponseEntity<ApiResponse<List<RoleDropdownResponse>>> dropdown() {
        List<RoleDropdownResponse> data = roleService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
