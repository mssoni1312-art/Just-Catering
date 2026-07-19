package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Role} search and filter queries.
 */
public final class RoleSpecification {

    private RoleSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for role search/filter.
     *
     * @param search free-text search across name/code/description
     * @param status optional status filter
     * @param system optional system-role filter
     * @return specification
     */
    public static Specification<Role> filter(String search, EntityStatus status, Boolean system) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern)
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (system != null) {
                predicates.add(cb.equal(root.get("system"), system));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
