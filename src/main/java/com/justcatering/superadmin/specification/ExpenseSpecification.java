package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Expense;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.ExpenseType;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Expense} search and filter queries.
 */
public final class ExpenseSpecification {

    private ExpenseSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for expense search/filter.
     *
     * @param search         free-text search
     * @param status         entity status
     * @param expenseType    expense type filter
     * @param clientUuid     client UUID filter
     * @param memberUserUuid member user UUID filter
     * @param expenseFrom    expense date from
     * @param expenseTo      expense date to
     * @param paidFrom       paid date from
     * @param paidTo         paid date to
     * @return specification
     */
    public static Specification<Expense> filter(
            String search,
            EntityStatus status,
            ExpenseType expenseType,
            UUID clientUuid,
            UUID memberUserUuid,
            LocalDate expenseFrom,
            LocalDate expenseTo,
            LocalDate paidFrom,
            LocalDate paidTo
    ) {
        return (root, query, cb) -> {
            if (Expense.class.equals(query.getResultType())) {
                root.fetch("client", JoinType.LEFT);
                root.fetch("memberUser", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("remarks"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("paymentMode"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("fromCity"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("toCity"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("accountContact"), "")), pattern),
                        cb.like(cb.lower(root.get("client").get("clientName")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("email"), "")), pattern),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("memberUser").get("firstName"), "")),
                                pattern
                        ),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("memberUser").get("lastName"), "")),
                                pattern
                        ),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("memberUser").get("email"), "")),
                                pattern
                        )
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (expenseType != null) {
                predicates.add(cb.equal(root.get("expenseType"), expenseType));
            }
            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (memberUserUuid != null) {
                predicates.add(cb.equal(root.get("memberUser").get("uuid"), memberUserUuid));
            }
            if (expenseFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("expenseDate"), expenseFrom));
            }
            if (expenseTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expenseDate"), expenseTo));
            }
            if (paidFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paidDate"), paidFrom));
            }
            if (paidTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paidDate"), paidTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
