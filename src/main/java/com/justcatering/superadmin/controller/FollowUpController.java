package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.FollowUpCreateRequest;
import com.justcatering.superadmin.dto.request.FollowUpUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.FollowUpDetailsResponse;
import com.justcatering.superadmin.dto.response.FollowUpDropdownResponse;
import com.justcatering.superadmin.dto.response.FollowUpListResponse;
import com.justcatering.superadmin.dto.response.FollowUpReminderOptionResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import com.justcatering.superadmin.service.FollowUpService;
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
 * REST controller for follow-up management APIs.
 */
@RestController
@RequestMapping(AppConstants.FOLLOW_UP_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Follow-ups", description = "Follow-up CRUD, search, filter, and dropdown APIs")
public class FollowUpController {

    private final FollowUpService followUpService;

    /**
     * Creates a follow-up for a specific client or lead.
     *
     * @param request create payload ({@code clientUuid} OR {@code leadUuid})
     * @return created follow-up
     */
    @PostMapping
    @PreAuthorize("hasAuthority('FOLLOWUP_MANAGE')")
    @Operation(
            summary = "Create follow-up",
            description = "Pass clientUuid for a client. Lead screens may also send a lead UUID in clientUuid."
    )
    public ResponseEntity<ApiResponse<FollowUpDetailsResponse>> create(
            @Valid @RequestBody FollowUpCreateRequest request
    ) {
        FollowUpDetailsResponse data = followUpService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates a follow-up.
     *
     * @param uuid    follow-up UUID
     * @param request update payload
     * @return updated follow-up
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('FOLLOWUP_MANAGE')")
    @Operation(
            summary = "Update follow-up",
            description = "Optionally re-bind with clientUuid OR leadUuid. Omit both to keep the current client/lead."
    )
    public ResponseEntity<ApiResponse<FollowUpDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody FollowUpUpdateRequest request
    ) {
        FollowUpDetailsResponse data = followUpService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns reminder preset options for the follow-up form.
     *
     * @return reminder options
     */
    @GetMapping("/reminders")
    @PreAuthorize("hasAnyAuthority('FOLLOWUP_VIEW', 'FOLLOWUP_MANAGE')")
    @Operation(summary = "Follow-up reminder options")
    public ResponseEntity<ApiResponse<List<FollowUpReminderOptionResponse>>> reminders() {
        List<FollowUpReminderOptionResponse> data = followUpService.reminderOptions();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns follow-up details.
     *
     * @param uuid follow-up UUID
     * @return follow-up details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    @Operation(summary = "Get follow-up by UUID")
    public ResponseEntity<ApiResponse<FollowUpDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        FollowUpDetailsResponse data = followUpService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a follow-up.
     *
     * @param uuid follow-up UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('FOLLOWUP_MANAGE')")
    @Operation(summary = "Delete follow-up")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        followUpService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }

    /**
     * Lists follow-ups with optional filters.
     * <p>
     * For a client screen use {@code clientUuid}. For a lead screen use {@code leadUuid}.
     * </p>
     *
     * @return page of follow-ups
     */
    @GetMapping
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    @Operation(
            summary = "List follow-ups",
            description = "Filter client-wise with clientUuid, or lead-wise with leadUuid"
    )
    public ResponseEntity<ApiResponse<PageResponse<FollowUpListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) FollowUpStatus status,
            @RequestParam(required = false) FollowUpType type,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID leadUuid,
            @RequestParam(required = false) UUID assignedUserUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<FollowUpListResponse> data = followUpService.search(
                search,
                status,
                type,
                clientUuid,
                leadUuid,
                assignedUserUuid,
                followUpFrom,
                followUpTo,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches follow-ups.
     *
     * @return page of follow-ups
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    @Operation(summary = "Search follow-ups")
    public ResponseEntity<ApiResponse<PageResponse<FollowUpListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID leadUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, clientUuid, leadUuid, null, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters follow-ups.
     *
     * @return page of follow-ups
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    @Operation(summary = "Filter follow-ups")
    public ResponseEntity<ApiResponse<PageResponse<FollowUpListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) FollowUpStatus status,
            @RequestParam(required = false) FollowUpType type,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID leadUuid,
            @RequestParam(required = false) UUID assignedUserUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(
                search, status, type, clientUuid, leadUuid, assignedUserUuid,
                followUpFrom, followUpTo, page, size, sortBy, direction
        );
    }

    /**
     * Returns active follow-ups for dropdowns.
     *
     * @return dropdown options
     */
    @GetMapping("/dropdown")
    @PreAuthorize("hasAnyAuthority('FOLLOWUP_VIEW', 'FOLLOWUP_MANAGE')")
    @Operation(summary = "Follow-up dropdown")
    public ResponseEntity<ApiResponse<List<FollowUpDropdownResponse>>> dropdown() {
        List<FollowUpDropdownResponse> data = followUpService.dropdown();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
