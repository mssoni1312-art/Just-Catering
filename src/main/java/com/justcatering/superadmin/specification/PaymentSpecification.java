package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Payment;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.PaymentMode;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Payment} search and filter queries.
 */
public final class PaymentSpecification {

    private PaymentSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for payment search/filter.
     *
     * @param search        free-text search
     * @param status        entity status
     * @param paymentMode   payment mode filter
     * @param clientUuid    client UUID filter
     * @param productUuid   product UUID filter
     * @param paymentFrom   payment date from
     * @param paymentTo     payment date to
     * @return specification
     */
    public static Specification<Payment> filter(
            String search,
            EntityStatus status,
            PaymentMode paymentMode,
            UUID clientUuid,
            UUID productUuid,
            LocalDate paymentFrom,
            LocalDate paymentTo
    ) {
        return (root, query, cb) -> {
            if (Payment.class.equals(query.getResultType())) {
                root.fetch("client", JoinType.LEFT);
                root.fetch("product", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("invoiceNumber")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("bankType"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("remarks"), "")), pattern),
                        cb.like(cb.lower(root.get("client").get("clientName")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("email"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("product").get("name"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("product").get("code"), "")), pattern)
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (paymentMode != null) {
                predicates.add(cb.equal(root.get("paymentMode"), paymentMode));
            }
            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (productUuid != null) {
                predicates.add(cb.equal(root.get("product").get("uuid"), productUuid));
            }
            if (paymentFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paymentDate"), paymentFrom));
            }
            if (paymentTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paymentDate"), paymentTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
