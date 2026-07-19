package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.ClientDeadline;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link ClientDeadline} search and filter queries.
 */
public final class ClientDeadlineSpecification {

    private ClientDeadlineSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for client deadline search/filter.
     *
     * @param search         free-text search
     * @param clientUuid     client UUID filter
     * @param departmentUuid department UUID filter
     * @return specification
     */
    public static Specification<ClientDeadline> filter(
            String search,
            UUID clientUuid,
            UUID departmentUuid
    ) {
        return (root, query, cb) -> {
            if (ClientDeadline.class.equals(query.getResultType())) {
                root.fetch("client", JoinType.LEFT);
                root.fetch("department", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("reason")), pattern),
                        cb.like(cb.lower(root.get("client").get("clientName")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("email"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("department").get("name"), "")), pattern)
                ));
            }

            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (departmentUuid != null) {
                predicates.add(cb.equal(root.get("department").get("uuid"), departmentUuid));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
