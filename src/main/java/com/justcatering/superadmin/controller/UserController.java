package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.UserCreateRequest;
import com.justcatering.superadmin.dto.request.UserUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.UserDetailsResponse;
import com.justcatering.superadmin.dto.response.UserDropdownResponse;
import com.justcatering.superadmin.dto.response.UserListResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.service.UserService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
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
 * REST controller for user management APIs.
 */
@RestController
@RequestMapping(AppConstants.USER_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "User CRUD, search, filter, and dropdown APIs")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param request create payload
     * @return created user
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @Operation(summary = "Create user")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> create(
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserDetailsResponse data = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates an existing user.
     *
     * @param uuid    user UUID
     * @param request update payload
     * @return updated user
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Update user")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        UserDetailsResponse data = userService.update(uuid, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", data, HttpStatus.OK.value()));
    }

    /**
     * Returns user details.
     *
     * @param uuid user UUID
     * @return user details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "Get user by UUID")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        UserDetailsResponse data = userService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a user.
     *
     * @param uuid user UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @Operation(summary = "Delete user")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        userService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists users with optional filters.
     *
     * @return page of users
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "List users")
    public ResponseEntity<ApiResponse<PageResponse<UserListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdTo,
            @RequestParam(required = false) Boolean excludeCurrentUser,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<UserListResponse> data = userService.search(
                search,
                status,
                roleCode,
                createdFrom,
                createdTo,
                Boolean.TRUE.equals(excludeCurrentUser),
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches users for member pickers (excludes the logged-in user by default).
     *
     * @return page of users
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "Search users for member selection")
    public ResponseEntity<ApiResponse<PageResponse<UserListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "true") Boolean excludeCurrentUser,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, null, null, excludeCurrentUser, page, size, sortBy, direction);
    }

    /**
     * Filters users by status, role, and date range.
     *
     * @return page of users
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "Filter users")
    public ResponseEntity<ApiResponse<PageResponse<UserListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdTo,
            @RequestParam(required = false) Boolean excludeCurrentUser,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, status, roleCode, createdFrom, createdTo, excludeCurrentUser, page, size, sortBy, direction);
    }

    /**
     * Returns active users for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "User dropdown")
    public ResponseEntity<ApiResponse<List<UserDropdownResponse>>> dropdown() {
        List<UserDropdownResponse> data = userService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
