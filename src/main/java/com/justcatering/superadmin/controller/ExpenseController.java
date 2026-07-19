package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.ExpenseCreateRequest;
import com.justcatering.superadmin.dto.request.ExpenseUpdateRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.ExpenseDetailsResponse;
import com.justcatering.superadmin.dto.response.ExpenseListResponse;
import com.justcatering.superadmin.dto.response.ExpenseSummaryResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.ExpenseType;
import com.justcatering.superadmin.service.ExpenseService;
import com.justcatering.superadmin.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
 * REST controller for expense management APIs.
 */
@RestController
@RequestMapping(AppConstants.EXPENSE_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Expenses", description = "Expense CRUD, search, filter, and summary APIs")
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Creates an expense.
     *
     * @param request create payload
     * @return created expense
     */
    @PostMapping
    @PreAuthorize("hasAuthority('EXPENSE_MANAGE')")
    @Operation(summary = "Create expense")
    public ResponseEntity<ApiResponse<ExpenseDetailsResponse>> create(
            @Valid @RequestBody ExpenseCreateRequest request
    ) {
        ExpenseDetailsResponse data = expenseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    /**
     * Updates an expense.
     *
     * @param uuid    expense UUID
     * @param request update payload
     * @return updated expense
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('EXPENSE_MANAGE')")
    @Operation(summary = "Update expense")
    public ResponseEntity<ApiResponse<ExpenseDetailsResponse>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody ExpenseUpdateRequest request
    ) {
        ExpenseDetailsResponse data = expenseService.update(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success("Resource updated successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Returns aggregated expense totals by type.
     *
     * @return summary totals
     */
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(summary = "Expense summary by type")
    public ResponseEntity<ApiResponse<ExpenseSummaryResponse>> summary(
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID memberUserUuid,
            @RequestParam(required = false) ExpenseType expenseType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseTo
    ) {
        ExpenseSummaryResponse data = expenseService.summary(
                clientUuid, memberUserUuid, expenseType, expenseFrom, expenseTo
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Lists expenses with optional filters.
     *
     * @return page of expenses
     */
    @GetMapping
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(summary = "List expenses")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseListResponse>>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) ExpenseType expenseType,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID memberUserUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paidFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paidTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<ExpenseListResponse> data = expenseService.search(
                search,
                status,
                expenseType,
                clientUuid,
                memberUserUuid,
                expenseFrom,
                expenseTo,
                paidFrom,
                paidTo,
                PageableUtil.of(page, size, sortBy, direction)
        );
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Searches expenses.
     *
     * @return page of expenses
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(summary = "Search expenses")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseListResponse>>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(search, null, null, null, null, null, null, null, null, page, size, sortBy, direction);
    }

    /**
     * Filters expenses.
     *
     * @return page of expenses
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(summary = "Filter expenses")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseListResponse>>> filter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EntityStatus status,
            @RequestParam(required = false) ExpenseType expenseType,
            @RequestParam(required = false) UUID clientUuid,
            @RequestParam(required = false) UUID memberUserUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paidFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paidTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        return list(
                search, status, expenseType, clientUuid, memberUserUuid,
                expenseFrom, expenseTo, paidFrom, paidTo, page, size, sortBy, direction
        );
    }

    /**
     * Returns expense details.
     *
     * @param uuid expense UUID
     * @return expense details
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(summary = "Get expense by UUID")
    public ResponseEntity<ApiResponse<ExpenseDetailsResponse>> getByUuid(@PathVariable UUID uuid) {
        ExpenseDetailsResponse data = expenseService.getByUuid(uuid);
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Soft-deletes an expense.
     *
     * @param uuid expense UUID
     * @return success response
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('EXPENSE_MANAGE')")
    @Operation(summary = "Delete expense")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        expenseService.delete(uuid);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", HttpStatus.OK.value()));
    }
}
