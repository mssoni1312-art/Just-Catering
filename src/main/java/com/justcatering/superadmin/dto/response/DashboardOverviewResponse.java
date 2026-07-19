package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dashboard overview metrics matching the Figma overview cards.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponse {

    private long meetingLeads;
    private long totalClients;
    private BigDecimal totalRevenue;
    private BigDecimal totalReceivable;
    private BigDecimal pendingAmount;
    private long totalQueries;
    private long totalSolvedQueries;
    private long totalPendingQueries;
}
