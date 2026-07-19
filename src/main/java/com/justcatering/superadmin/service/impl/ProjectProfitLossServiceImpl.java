package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.response.ProjectProfitLossResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.ExpenseRepository;
import com.justcatering.superadmin.service.ProjectProfitLossService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ProjectProfitLossService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectProfitLossServiceImpl implements ProjectProfitLossService {

    private static final int MARGIN_SCALE = 2;

    private final ClientRepository clientRepository;
    private final ExpenseRepository expenseRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectProfitLossResponse getProfitLoss(UUID clientUuid) {
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));

        BigDecimal income = client.getTotalAmount() != null ? client.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal totalExpenses = expenseRepository.sumTotalAmount(clientUuid, null, null, null, null);
        if (totalExpenses == null) {
            totalExpenses = BigDecimal.ZERO;
        }

        BigDecimal profit = income.subtract(totalExpenses);
        BigDecimal profitMarginPercent = percentOf(profit, income);
        BigDecimal expenseRatioPercent = percentOf(totalExpenses, income);

        log.debug("Computed P&L for client {}: income={}, expenses={}, profit={}",
                client.getClientName(), income, totalExpenses, profit);

        return ProjectProfitLossResponse.builder()
                .clientUuid(client.getUuid())
                .clientName(client.getClientName())
                .income(income)
                .totalExpenses(totalExpenses)
                .profit(profit)
                .profitMarginPercent(profitMarginPercent)
                .expenseRatioPercent(expenseRatioPercent)
                .build();
    }

    private BigDecimal percentOf(BigDecimal part, BigDecimal whole) {
        if (whole == null || whole.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(MARGIN_SCALE, RoundingMode.HALF_UP);
        }
        return part.multiply(BigDecimal.valueOf(100))
                .divide(whole, MARGIN_SCALE, RoundingMode.HALF_UP);
    }
}
