package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Client} search and filter queries.
 */
public final class ClientSpecification {

    private ClientSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for client search/filter.
     *
     * @param search      free-text search
     * @param status      entity status
     * @param clientStage stage filter
     * @param priority    priority filter
     * @param productUuid product UUID filter
     * @param clientType  client type filter
     * @param dealFrom    deal date from
     * @param dealTo      deal date to
     * @return specification
     */
    public static Specification<Client> filter(
            String search,
            EntityStatus status,
            ClientStage clientStage,
            Priority priority,
            UUID productUuid,
            String clientType,
            LocalDate dealFrom,
            LocalDate dealTo
    ) {
        return (root, query, cb) -> {
            if (Client.class.equals(query.getResultType())) {
                root.fetch("product", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Join<Object, Object> product = root.join("product", JoinType.LEFT);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("clientName")), pattern),
                        cb.like(cb.lower(root.get("contactPerson")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("mobile"), "")), pattern),
                        cb.like(cb.lower(root.get("clientType")), pattern),
                        cb.like(cb.lower(cb.coalesce(product.get("name"), "")), pattern)
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (clientStage != null) {
                predicates.add(cb.equal(root.get("clientStage"), clientStage));
            }
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            if (productUuid != null) {
                Join<Object, Object> product = root.join("product", JoinType.INNER);
                predicates.add(cb.equal(product.get("uuid"), productUuid));
            }
            if (StringUtils.hasText(clientType)) {
                predicates.add(cb.equal(cb.lower(root.get("clientType")), clientType.trim().toLowerCase()));
            }
            if (dealFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dealDate"), dealFrom));
            }
            if (dealTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dealDate"), dealTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
