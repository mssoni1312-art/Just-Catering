package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.response.DashboardOverviewResponse;

/**
 * Dashboard overview service contract.
 */
public interface DashboardService {

    /**
     * Returns aggregated dashboard overview metrics.
     *
     * @return overview counts and amounts
     */
    DashboardOverviewResponse getOverview();
}
