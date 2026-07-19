package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.DashboardOverviewResponse;
import com.justcatering.superadmin.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for dashboard overview APIs.
 */
@RestController
@RequestMapping(AppConstants.DASHBOARD_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard", description = "Dashboard overview APIs")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Returns aggregated dashboard overview metrics.
     *
     * @return overview counts and amounts
     */
    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(summary = "Get dashboard overview")
    public ResponseEntity<ApiResponse<DashboardOverviewResponse>> getOverview() {
        DashboardOverviewResponse data = dashboardService.getOverview();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }
}
