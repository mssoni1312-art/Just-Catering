package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.ExpenseCreateRequest;
import com.justcatering.superadmin.dto.request.ExpenseUpdateRequest;
import com.justcatering.superadmin.dto.response.ExpenseDetailsResponse;
import com.justcatering.superadmin.dto.response.ExpenseListResponse;
import com.justcatering.superadmin.dto.response.ExpenseSummaryResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.Expense;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.ExpenseType;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.ExpenseMapper;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.ExpenseRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.ExpenseService;
import com.justcatering.superadmin.specification.ExpenseSpecification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ExpenseService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpenseDetailsResponse create(ExpenseCreateRequest request) {
        Expense expense = Expense.builder()
                .client(resolveOptionalClient(request.getClientUuid()))
                .title(request.getTitle().trim())
                .expenseType(request.getExpenseType())
                .memberUser(resolveOptionalUser(request.getMemberUserUuid()))
                .expenseDate(request.getExpenseDate())
                .paidDate(request.getPaidDate())
                .dueDate(request.getDueDate())
                .amount(request.getAmount())
                .paymentMode(normalizeOptional(request.getPaymentMode()))
                .fromCity(normalizeOptional(request.getFromCity()))
                .toCity(normalizeOptional(request.getToCity()))
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .km(request.getKm())
                .accountContact(normalizeOptional(request.getAccountContact()))
                .remarks(normalizeOptional(request.getRemarks()))
                .billUrl(normalizeOptional(request.getBillUrl()))
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        expense = expenseRepository.save(expense);
        log.info("Created expense: {}", expense.getTitle());
        return expenseMapper.toDetails(reloadWithRelations(expense.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpenseDetailsResponse update(UUID uuid, ExpenseUpdateRequest request) {
        Expense expense = findOrThrow(uuid);

        expense.setClient(resolveOptionalClient(request.getClientUuid()));
        expense.setTitle(request.getTitle().trim());
        expense.setExpenseType(request.getExpenseType());
        expense.setMemberUser(resolveOptionalUser(request.getMemberUserUuid()));
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaidDate(request.getPaidDate());
        expense.setDueDate(request.getDueDate());
        expense.setAmount(request.getAmount());
        expense.setPaymentMode(normalizeOptional(request.getPaymentMode()));
        expense.setFromCity(normalizeOptional(request.getFromCity()));
        expense.setToCity(normalizeOptional(request.getToCity()));
        expense.setFromDate(request.getFromDate());
        expense.setToDate(request.getToDate());
        expense.setKm(request.getKm());
        expense.setAccountContact(normalizeOptional(request.getAccountContact()));
        expense.setRemarks(normalizeOptional(request.getRemarks()));
        expense.setBillUrl(normalizeOptional(request.getBillUrl()));
        if (request.getStatus() != null) {
            expense.setStatus(request.getStatus());
        }

        expenseRepository.save(expense);
        log.info("Updated expense: {}", expense.getTitle());
        return expenseMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ExpenseDetailsResponse getByUuid(UUID uuid) {
        return expenseMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Expense expense = findOrThrow(uuid);
        expense.softDelete();
        expenseRepository.save(expense);
        log.info("Soft-deleted expense: {}", expense.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ExpenseListResponse> search(
            String search,
            EntityStatus status,
            ExpenseType expenseType,
            UUID clientUuid,
            UUID memberUserUuid,
            LocalDate expenseFrom,
            LocalDate expenseTo,
            LocalDate paidFrom,
            LocalDate paidTo,
            Pageable pageable
    ) {
        Page<Expense> page = expenseRepository.findAll(
                ExpenseSpecification.filter(
                        search, status, expenseType, clientUuid, memberUserUuid,
                        expenseFrom, expenseTo, paidFrom, paidTo
                ),
                pageable
        );
        return PageResponse.from(page.map(expenseMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ExpenseSummaryResponse summary(
            UUID clientUuid,
            UUID memberUserUuid,
            ExpenseType expenseType,
            LocalDate expenseFrom,
            LocalDate expenseTo
    ) {
        List<Object[]> typeTotals = expenseRepository.sumAmountsByType(
                clientUuid, memberUserUuid, expenseType, expenseFrom, expenseTo
        );
        BigDecimal total = expenseRepository.sumTotalAmount(
                clientUuid, memberUserUuid, expenseType, expenseFrom, expenseTo
        );

        ExpenseSummaryResponse response = ExpenseSummaryResponse.builder()
                .travel(BigDecimal.ZERO)
                .food(BigDecimal.ZERO)
                .office(BigDecimal.ZERO)
                .other(BigDecimal.ZERO)
                .total(total != null ? total : BigDecimal.ZERO)
                .build();

        for (Object[] row : typeTotals) {
            ExpenseType type = (ExpenseType) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            switch (type) {
                case TRAVEL -> response.setTravel(amount);
                case FOOD -> response.setFood(amount);
                case OFFICE -> response.setOffice(amount);
                case OTHER -> response.setOther(amount);
                default -> {
                    // No-op
                }
            }
        }

        return response;
    }

    private Client resolveOptionalClient(UUID clientUuid) {
        if (clientUuid == null) {
            return null;
        }
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected client is not active");
        }
        return client;
    }

    private User resolveOptionalUser(UUID userUuid) {
        if (userUuid == null) {
            return null;
        }
        User user = userRepository.findByUuidAndDeletedFalse(userUuid)
                .orElseThrow(() -> new EntityNotFoundException("User", userUuid));
        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected user is not active");
        }
        return user;
    }

    private Expense findOrThrow(UUID uuid) {
        return expenseRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Expense", uuid));
    }

    private Expense reloadWithRelations(UUID uuid) {
        return expenseRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Expense", uuid));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
