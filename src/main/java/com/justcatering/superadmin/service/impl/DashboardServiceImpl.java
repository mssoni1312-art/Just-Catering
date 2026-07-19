package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.response.DashboardOverviewResponse;
import com.justcatering.superadmin.enums.QueryStatus;
import com.justcatering.superadmin.repository.ClientQueryRepository;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.LeadRepository;
import com.justcatering.superadmin.repository.PaymentRepository;
import com.justcatering.superadmin.service.DashboardService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link DashboardService}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private static final List<QueryStatus> SOLVED_STATUSES = List.of(QueryStatus.SOLVED, QueryStatus.COMPLETED);
    private static final List<QueryStatus> PENDING_STATUSES = List.of(
            QueryStatus.PENDING,
            QueryStatus.IN_PROGRESS,
            QueryStatus.NEEDS_ATTENTION
    );

    private final LeadRepository leadRepository;
    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;
    private final ClientQueryRepository clientQueryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public DashboardOverviewResponse getOverview() {
        long meetingLeads = leadRepository.countByDeletedFalse();
        long totalClients = clientRepository.countByDeletedFalse();

        BigDecimal totalRevenue = clientRepository.sumBudgetByDeletedFalse();
        BigDecimal totalDealAmount = clientRepository.sumTotalAmountByDeletedFalse();
        BigDecimal totalCollected = paymentRepository.sumAmountByDeletedFalse();
        BigDecimal outstandingBalance = totalDealAmount.subtract(totalCollected);

        long totalQueries = clientQueryRepository.countByDeletedFalse();
        long totalSolvedQueries = clientQueryRepository.countByDeletedFalseAndQueryStatusIn(SOLVED_STATUSES);
        long totalPendingQueries = clientQueryRepository.countByDeletedFalseAndQueryStatusIn(PENDING_STATUSES);

        return DashboardOverviewResponse.builder()
                .meetingLeads(meetingLeads)
                .totalClients(totalClients)
                .totalRevenue(totalRevenue)
                .totalReceivable(totalCollected)
                .pendingAmount(outstandingBalance)
                .totalQueries(totalQueries)
                .totalSolvedQueries(totalSolvedQueries)
                .totalPendingQueries(totalPendingQueries)
                .build();
    }
}
