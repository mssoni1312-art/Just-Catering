package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link User} search and filter queries.
 */
public final class UserSpecification {

    private UserSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for user search/filter.
     *
     * @param search    free-text search across name/email/phone/company
     * @param status    optional status filter
     * @param roleCode  optional role code filter
     * @param createdFrom optional created-from timestamp
     * @param createdTo   optional created-to timestamp
     * @return specification
     */
    public static Specification<User> filter(
            String search,
            EntityStatus status,
            String roleCode,
            Instant createdFrom,
            Instant createdTo
    ) {
        return filter(search, status, roleCode, createdFrom, createdTo, null);
    }

    /**
     * Builds a specification for user search/filter.
     *
     * @param search         free-text search across name/email/phone/company
     * @param status         optional status filter
     * @param roleCode       optional role code filter
     * @param createdFrom    optional created-from timestamp
     * @param createdTo      optional created-to timestamp
     * @param excludeUserId  optional user id to exclude (e.g. current user)
     * @return specification
     */
    public static Specification<User> filter(
            String search,
            EntityStatus status,
            String roleCode,
            Instant createdFrom,
            Instant createdTo,
            Long excludeUserId
    ) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (excludeUserId != null) {
                predicates.add(cb.notEqual(root.get("id"), excludeUserId));
            }

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("phone"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("companyName"), "")), pattern)
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (StringUtils.hasText(roleCode)) {
                Join<Object, Object> userRoles = root.join("userRoles", JoinType.INNER);
                Join<Object, Object> role = userRoles.join("role", JoinType.INNER);
                predicates.add(cb.isFalse(userRoles.get("deleted")));
                predicates.add(cb.isFalse(role.get("deleted")));
                predicates.add(cb.equal(role.get("code"), roleCode.trim().toUpperCase()));
            }

            if (createdFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
            }
            if (createdTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
