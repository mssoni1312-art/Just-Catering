package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Requirement;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Requirement} search and filter queries.
 */
public final class RequirementSpecification {

    private RequirementSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for requirement search/filter.
     *
     * @param search           free-text search
     * @param clientUuid       client UUID filter
     * @param assignedUserUuid assigned user UUID filter
     * @return specification
     */
    public static Specification<Requirement> filter(
            String search,
            UUID clientUuid,
            UUID assignedUserUuid
    ) {
        return (root, query, cb) -> {
            if (Requirement.class.equals(query.getResultType())) {
                root.fetch("client", JoinType.LEFT);
                root.fetch("assignedUser", JoinType.LEFT);
                root.fetch("documents", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("notes"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("checkInAddress"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("checkOutAddress"), "")), pattern),
                        cb.like(cb.lower(root.get("client").get("clientName")), pattern),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("assignedUser").get("firstName"), "")),
                                pattern
                        ),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("assignedUser").get("lastName"), "")),
                                pattern
                        )
                ));
            }

            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (assignedUserUuid != null) {
                predicates.add(cb.equal(root.get("assignedUser").get("uuid"), assignedUserUuid));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
