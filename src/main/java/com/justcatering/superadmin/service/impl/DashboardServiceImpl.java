package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.response.DashboardOverviewResponse;
import com.justcatering.superadmin.enums.QueryStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.repository.ClientQueryRepository;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.LeadRepository;
import com.justcatering.superadmin.repository.PaymentRepository;
import com.justcatering.superadmin.service.DashboardService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
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
    public DashboardOverviewResponse getOverview(Integer month, Integer year) {
        if (month == null && year == null) {
            return buildAllTimeOverview();
        }
        if (month == null || year == null) {
            throw new BusinessException("Both month and year are required for month filtering");
        }
        if (month < 1 || month > 12) {
            throw new BusinessException("month must be between 1 and 12");
        }
        if (year < 2000 || year > 2100) {
            throw new BusinessException("year is out of supported range");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate fromDate = yearMonth.atDay(1);
        LocalDate toDate = yearMonth.atEndOfMonth();
        Instant fromInclusive = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toExclusive = yearMonth.plusMonths(1).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        long meetingLeads = leadRepository.countCreatedBetween(fromInclusive, toExclusive);
        long totalClients = clientRepository.countInPeriod(fromDate, toDate, fromInclusive, toExclusive);

        BigDecimal totalRevenue = clientRepository.sumBudgetInPeriod(
                fromDate, toDate, fromInclusive, toExclusive
        );
        BigDecimal totalDealAmount = clientRepository.sumTotalAmountInPeriod(
                fromDate, toDate, fromInclusive, toExclusive
        );
        BigDecimal totalCollected = paymentRepository.sumAmountByPaymentDateBetween(fromDate, toDate);
        BigDecimal outstandingBalance = totalDealAmount.subtract(totalCollected);

        long totalQueries = clientQueryRepository.countCreatedBetween(fromInclusive, toExclusive);
        long totalSolvedQueries = clientQueryRepository.countCreatedBetweenAndQueryStatusIn(
                SOLVED_STATUSES, fromInclusive, toExclusive
        );
        long totalPendingQueries = clientQueryRepository.countCreatedBetweenAndQueryStatusIn(
                PENDING_STATUSES, fromInclusive, toExclusive
        );

        return DashboardOverviewResponse.builder()
                .meetingLeads(meetingLeads)
                .totalClients(totalClients)
                .totalRevenue(nullToZero(totalRevenue))
                .totalReceivable(nullToZero(totalCollected))
                .pendingAmount(nullToZero(outstandingBalance))
                .totalQueries(totalQueries)
                .totalSolvedQueries(totalSolvedQueries)
                .totalPendingQueries(totalPendingQueries)
                .build();
    }

    private DashboardOverviewResponse buildAllTimeOverview() {
        long meetingLeads = leadRepository.countByDeletedFalse();
        long totalClients = clientRepository.countByDeletedFalse();

        BigDecimal totalRevenue = clientRepository.sumBudgetByDeletedFalse();
        BigDecimal totalDealAmount = clientRepository.sumTotalAmountByDeletedFalse();
        BigDecimal totalCollected = paymentRepository.sumAmountByDeletedFalse();
        BigDecimal outstandingBalance = nullToZero(totalDealAmount).subtract(nullToZero(totalCollected));

        long totalQueries = clientQueryRepository.countByDeletedFalse();
        long totalSolvedQueries = clientQueryRepository.countByDeletedFalseAndQueryStatusIn(SOLVED_STATUSES);
        long totalPendingQueries = clientQueryRepository.countByDeletedFalseAndQueryStatusIn(PENDING_STATUSES);

        return DashboardOverviewResponse.builder()
                .meetingLeads(meetingLeads)
                .totalClients(totalClients)
                .totalRevenue(nullToZero(totalRevenue))
                .totalReceivable(nullToZero(totalCollected))
                .pendingAmount(outstandingBalance)
                .totalQueries(totalQueries)
                .totalSolvedQueries(totalSolvedQueries)
                .totalPendingQueries(totalPendingQueries)
                .build();
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
