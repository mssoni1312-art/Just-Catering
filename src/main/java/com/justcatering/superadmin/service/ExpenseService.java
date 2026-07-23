package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ExpenseCategoryCreateRequest;
import com.justcatering.superadmin.dto.request.ExpenseCreateRequest;
import com.justcatering.superadmin.dto.request.ExpenseUpdateRequest;
import com.justcatering.superadmin.dto.response.ExpenseCategoryResponse;
import com.justcatering.superadmin.dto.response.ExpenseDetailsResponse;
import com.justcatering.superadmin.dto.response.ExpenseListResponse;
import com.justcatering.superadmin.dto.response.ExpenseSummaryResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.ExpenseType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Expense management service contract.
 */
public interface ExpenseService {

    /**
     * Creates an expense.
     *
     * @param request create payload
     * @return created expense details
     */
    ExpenseDetailsResponse create(ExpenseCreateRequest request);

    /**
     * Updates an expense.
     *
     * @param uuid    expense UUID
     * @param request update payload
     * @return updated expense details
     */
    ExpenseDetailsResponse update(UUID uuid, ExpenseUpdateRequest request);

    /**
     * Returns expense details by UUID.
     *
     * @param uuid expense UUID
     * @return expense details
     */
    ExpenseDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes an expense.
     *
     * @param uuid expense UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters expenses.
     *
     * @param search         free-text search
     * @param status         entity status
     * @param expenseType    expense type filter
     * @param clientUuid     client filter
     * @param memberUserUuid member user filter
     * @param expenseFrom    expense date from
     * @param expenseTo      expense date to
     * @param paidFrom       paid date from
     * @param paidTo         paid date to
     * @param pageable       pagination
     * @return page of expenses
     */
    PageResponse<ExpenseListResponse> search(
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
    );

    /**
     * Returns aggregated expense totals by category.
     *
     * @param clientUuid     optional client filter
     * @param memberUserUuid optional member user filter
     * @param expenseType    optional expense type filter
     * @param expenseFrom    optional expense date from
     * @param expenseTo      optional expense date to
     * @return summary totals
     */
    ExpenseSummaryResponse summary(
            UUID clientUuid,
            UUID memberUserUuid,
            ExpenseType expenseType,
            LocalDate expenseFrom,
            LocalDate expenseTo
    );

    /**
     * Lists active expense categories for Overall Expenses.
     *
     * @return categories
     */
    List<ExpenseCategoryResponse> listCategories();

    /**
     * Creates an expense category (Add Category).
     *
     * @param request create payload
     * @return created category
     */
    ExpenseCategoryResponse createCategory(ExpenseCategoryCreateRequest request);
}
