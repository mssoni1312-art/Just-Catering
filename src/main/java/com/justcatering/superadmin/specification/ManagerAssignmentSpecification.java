package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.ClientManagerAssignment;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link ClientManagerAssignment} search and filter queries.
 */
public final class ManagerAssignmentSpecification {

    private ManagerAssignmentSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for manager assignment search/filter.
     *
     * @param search         free-text search
     * @param clientUuid     client UUID filter
     * @param userUuid       assigned user UUID filter
     * @param departmentUuid department UUID filter
     * @return specification
     */
    public static Specification<ClientManagerAssignment> filter(
            String search,
            UUID clientUuid,
            UUID userUuid,
            UUID departmentUuid
    ) {
        return (root, query, cb) -> {
            if (ClientManagerAssignment.class.equals(query.getResultType())) {
                root.fetch("client", JoinType.LEFT);
                root.fetch("user", JoinType.LEFT);
                root.fetch("department", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(cb.coalesce(root.get("projectName"), "")), pattern),
                        cb.like(cb.lower(root.get("client").get("clientName")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("email"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("user").get("firstName"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("user").get("lastName"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("user").get("email"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("department").get("name"), "")), pattern)
                ));
            }

            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (userUuid != null) {
                predicates.add(cb.equal(root.get("user").get("uuid"), userUuid));
            }
            if (departmentUuid != null) {
                predicates.add(cb.equal(root.get("department").get("uuid"), departmentUuid));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
