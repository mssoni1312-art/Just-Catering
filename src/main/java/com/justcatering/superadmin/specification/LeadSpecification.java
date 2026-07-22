package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Lead;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Lead} search and filter queries.
 */
public final class LeadSpecification {

    private LeadSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for lead search/filter.
     *
     * @param search      free-text search
     * @param status      entity status
     * @param leadStage   stage filter
     * @param productUuid product UUID filter
     * @param state       state filter
     * @param city        city filter
     * @param budgetFrom  minimum approximate budget
     * @param budgetTo    maximum approximate budget
     * @return specification
     */
    public static Specification<Lead> filter(
            String search,
            EntityStatus status,
            LeadStage leadStage,
            UUID productUuid,
            String state,
            String city,
            BigDecimal budgetFrom,
            BigDecimal budgetTo
    ) {
        return (root, query, cb) -> {
            if (Lead.class.equals(query.getResultType())) {
                root.fetch("product", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Join<Object, Object> product = root.join("product", JoinType.LEFT);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("email"), "")), pattern),
                        cb.like(cb.lower(root.get("companyName")), pattern),
                        cb.like(cb.lower(root.get("phone")), pattern),
                        cb.like(cb.lower(root.get("address")), pattern),
                        cb.like(cb.lower(root.get("state")), pattern),
                        cb.like(cb.lower(root.get("city")), pattern),
                        cb.like(cb.lower(cb.coalesce(product.get("name"), "")), pattern)
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (leadStage != null) {
                predicates.add(cb.equal(root.get("leadStage"), leadStage));
            }
            if (productUuid != null) {
                Join<Object, Object> product = root.join("product", JoinType.INNER);
                predicates.add(cb.equal(product.get("uuid"), productUuid));
            }
            if (StringUtils.hasText(state)) {
                predicates.add(cb.equal(cb.lower(root.get("state")), state.trim().toLowerCase()));
            }
            if (StringUtils.hasText(city)) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.trim().toLowerCase()));
            }
            if (budgetFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("approxBudget"), budgetFrom));
            }
            if (budgetTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("approxBudget"), budgetTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
