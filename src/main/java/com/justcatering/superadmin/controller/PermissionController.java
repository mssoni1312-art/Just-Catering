package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.PermissionDropdownResponse;
import com.justcatering.superadmin.dto.response.PermissionListResponse;
import com.justcatering.superadmin.dto.response.PermissionModuleResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.service.PermissionService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for permission query APIs.
 */
@RestController
@RequestMapping(AppConstants.PERMISSION_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Permissions", description = "Permission search, dropdown, and module grouping APIs")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Lists permissions with optional filters.
     *
     * @return page of permissions
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW', 'ROLE_MANAGE')")
    @Operation(summary = "List permissions")
    public ResponseEntity<ApiResponse<PageResponse<PermissionListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<PermissionListResponse> data = permissionService.search(
                search,
                module,
                status,
                PageableUtil.of(page, size, sortBy != null ? sortBy : "module", direction != null ? direction : "ASC")
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches permissions.
     *
     * @return page of permissions
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW', 'ROLE_MANAGE')")
    @Operation(summary = "Search permissions")
    public ResponseEntity<ApiResponse<PageResponse<PermissionListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return list(search, null, null, page, size, "module", "ASC");
    }

    /**
     * Filters permissions.
     *
     * @return page of permissions
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW', 'ROLE_MANAGE')")
    @Operation(summary = "Filter permissions")
    public ResponseEntity<ApiResponse<PageResponse<PermissionListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return list(search, module, status, page, size, "module", "ASC");
    }

    /**
     * Returns active permissions for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW', 'ROLE_MANAGE')")
    @Operation(summary = "Permission dropdown")
    public ResponseEntity<ApiResponse<List<PermissionDropdownResponse>>> dropdown() {
        List<PermissionDropdownResponse> data = permissionService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns permissions grouped by module for RBAC UI.
     *
     * @return module groups
     */
    @GetMapping("/modules")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW', 'ROLE_MANAGE')")
    @Operation(summary = "Permissions by module")
    public ResponseEntity<ApiResponse<List<PermissionModuleResponse>>> modules() {
        List<PermissionModuleResponse> data = permissionService.groupByModule();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
