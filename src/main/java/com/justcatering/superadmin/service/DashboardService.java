package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.response.DashboardOverviewResponse;

/**
 * Dashboard overview service contract.
 */
public interface DashboardService {

    /**
     * Returns aggregated dashboard overview metrics.
     *
     * <p>When both {@code month} and {@code year} are provided, metrics are scoped to that
     * calendar month. When both are omitted, all-time totals are returned.
     *
     * @param month optional month (1-12)
     * @param year  optional year (e.g. 2026)
     * @return overview counts and amounts
     */
    DashboardOverviewResponse getOverview(Integer month, Integer year);
}
