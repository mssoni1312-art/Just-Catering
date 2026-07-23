package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.OnSiteTaskPlanPrepareRequest;
import com.justcatering.superadmin.dto.request.OnSiteTaskPlanSaveRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanDetailsResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanListResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanPrepareResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.service.OnSiteTaskPlanService;
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
 * REST controller for on-site manager task plan APIs.
 * <p>
 * Screen 1 ({@code /prepare}): assign member + day count → returns day slots for next screen.<br>
 * Screen 2 ({@code POST /}): fill day tasks + save task plan.
 * </p>
 */
@RestController
@RequestMapping(AppConstants.ONSITE_TASK_PLAN_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "On-site Task Plans", description = "Create days task plan and save on-site manager tasks")
public class OnSiteTaskPlanController {

    private final OnSiteTaskPlanService onSiteTaskPlanService;

    /**
     * Screen 1 Add — returns empty day slots for the next form.
     *
     * @param request assign-to member and number of days
     * @return day slots for the on-site manager form
     */
    @PostMapping("/prepare")
    @PreAuthorize("hasAuthority('ONSITE_TASK_PLAN_MANAGE')")
    @Operation(
            summary = "Prepare day slots for next screen",
            description = "Create Days Task Plan modal: assign member + enter days, then open form with those day slots"
    )
    public ResponseEntity<ApiResponse<OnSiteTaskPlanPrepareResponse>> prepare(
            @Valid @RequestBody OnSiteTaskPlanPrepareRequest request
    ) {
        OnSiteTaskPlanPrepareResponse data = onSiteTaskPlanService.prepare(request);
        return ResponseEntity.ok(
                ApiResponse.success("Day slots prepared successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Screen 2 Save Task Plan.
     *
     * @param request manager, days, and optional notes
     * @return saved plan
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ONSITE_TASK_PLAN_MANAGE')")
    @Operation(summary = "Save on-site manager task plan")
    public ResponseEntity<ApiResponse<OnSiteTaskPlanDetailsResponse>> create(
            @Valid @RequestBody OnSiteTaskPlanSaveRequest request
    ) {
        OnSiteTaskPlanDetailsResponse data = onSiteTaskPlanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Lists saved task plans for the card screen (manager-wise).
     * <p>
     * Filter with {@code managerUuid} to show plans for one manager.
     * Each item includes plan date, manager name/initials/designation, and
     * {@code planLabel} such as {@code 4-Day Task Plan}.
     * </p>
     *
     * @return page of task plan cards
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ONSITE_TASK_PLAN_VIEW')")
    @Operation(
            summary = "List saved on-site task plans",
            description = "Returns task plan cards manager-wise. Optional managerUuid / clientUuid / search filters."
    )
    public ResponseEntity<ApiResponse<PageResponse<OnSiteTaskPlanListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID managerUuid,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<OnSiteTaskPlanListResponse> data = onSiteTaskPlanService.search(
                search,
                clientUuid,
                managerUuid,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns task plan details.
     *
     * @param uuid plan UUID
     * @return plan details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ONSITE_TASK_PLAN_VIEW')")
    @Operation(summary = "Get on-site task plan by UUID")
    public ResponseEntity<ApiResponse<OnSiteTaskPlanDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        OnSiteTaskPlanDetailsResponse data = onSiteTaskPlanService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Updates a task plan.
     *
     * @param uuid    plan UUID
     * @param request save payload
     * @return updated plan
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ONSITE_TASK_PLAN_MANAGE')")
    @Operation(summary = "Update on-site task plan")
    public ResponseEntity<ApiResponse<OnSiteTaskPlanDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody OnSiteTaskPlanSaveRequest request
    ) {
        OnSiteTaskPlanDetailsResponse data = onSiteTaskPlanService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes a task plan.
     *
     * @param uuid plan UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ONSITE_TASK_PLAN_MANAGE')")
    @Operation(summary = "Delete on-site task plan")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        onSiteTaskPlanService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }
}
