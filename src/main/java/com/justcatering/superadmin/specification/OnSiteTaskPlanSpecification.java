package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.OnSiteTaskPlan;
import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA specifications for filtering on-site task plans.
 */
public final class OnSiteTaskPlanSpecification {

    private OnSiteTaskPlanSpecification() {
        // Utility class
    }

    /**
     * Builds a filter specification for list/search.
     *
     * @param search      optional free-text search
     * @param clientUuid  optional client filter
     * @param managerUuid optional manager filter
     * @return specification
     */
    public static Specification<OnSiteTaskPlan> filter(String search, UUID clientUuid, UUID managerUuid) {
        return (root, query, cb) -> {
            if (query != null && Long.class != query.getResultType()) {
                root.fetch("manager", JoinType.LEFT);
                root.fetch("client", JoinType.LEFT);
                query.distinct(true);
            }

            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (managerUuid != null) {
                predicates.add(cb.equal(root.get("manager").get("uuid"), managerUuid));
            }
            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("manager").get("firstName")), pattern),
                        cb.like(cb.lower(root.get("manager").get("lastName")), pattern),
                        cb.like(cb.lower(root.get("additionalNotes")), pattern)
                ));
            }

            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
