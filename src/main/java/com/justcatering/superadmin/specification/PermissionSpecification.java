package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Permission} search and filter queries.
 */
public final class PermissionSpecification {

    private PermissionSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for permission search/filter.
     *
     * @param search free-text search across name/code/description
     * @param module optional module filter
     * @param status optional status filter
     * @return specification
     */
    public static Specification<Permission> filter(String search, String module, EntityStatus status) {
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

            if (StringUtils.hasText(module)) {
                predicates.add(cb.equal(cb.upper(root.get("module")), module.trim().toUpperCase()));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
