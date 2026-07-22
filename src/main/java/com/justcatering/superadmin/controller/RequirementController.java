package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.RequirementCreateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.RequirementDetailsResponse;
import com.justcatering.superadmin.dto.response.RequirementListResponse;
import com.justcatering.superadmin.service.RequirementService;
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
 * REST controller for on-site requirement APIs.
 */
@RestController
@RequestMapping(AppConstants.REQUIREMENT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Requirements", description = "On-site requirement list, create, and update APIs")
public class RequirementController {

    private final RequirementService requirementService;

    /**
     * Creates an on-site requirement.
     *
     * @param request create payload
     * @return created requirement
     */
    @PostMapping
    @PreAuthorize("hasAuthority('REQUIREMENT_MANAGE')")
    @Operation(
            summary = "Create on-site requirement",
            description = "Accepts optional voiceRecord { url, durationSeconds, fileName, contentType }"
    )
    public ResponseEntity<ApiResponse<RequirementDetailsResponse>> create(
            @Valid @RequestBody RequirementCreateRequest request
    ) {
        RequirementDetailsResponse data = requirementService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Lists on-site requirements with optional filters.
     *
     * @return page of requirements
     */
    @GetMapping
    @PreAuthorize("hasAuthority('REQUIREMENT_VIEW')")
    @Operation(summary = "List on-site requirements")
    public ResponseEntity<ApiResponse<PageResponse<RequirementListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID assignedUserUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<RequirementListResponse> data = requirementService.search(
                search,
                clientUuid,
                assignedUserUuid,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns on-site requirement details.
     *
     * @param uuid requirement UUID
     * @return requirement details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('REQUIREMENT_VIEW')")
    @Operation(summary = "Get on-site requirement by UUID")
    public ResponseEntity<ApiResponse<RequirementDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        RequirementDetailsResponse data = requirementService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Updates an on-site requirement.
     *
     * @param uuid    requirement UUID
     * @param request update payload
     * @return updated requirement
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('REQUIREMENT_MANAGE')")
    @Operation(
            summary = "Update on-site requirement",
            description = "Accepts optional voiceRecord { url, durationSeconds, fileName, contentType }"
    )
    public ResponseEntity<ApiResponse<RequirementDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody RequirementCreateRequest request
    ) {
        RequirementDetailsResponse data = requirementService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes an on-site requirement.
     *
     * @param uuid requirement UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('REQUIREMENT_MANAGE')")
    @Operation(summary = "Delete on-site requirement")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        requirementService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }
}
