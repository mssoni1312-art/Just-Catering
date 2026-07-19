package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Department} search and filter queries.
 */
public final class DepartmentSpecification {

    private DepartmentSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for department search/filter.
     *
     * @param search     free-text across name/code/description/member
     * @param status     optional status filter
     * @param parentUuid optional parent UUID filter
     * @return specification
     */
    public static Specification<Department> filter(String search, EntityStatus status, String parentUuid) {
        return (root, query, cb) -> {
            if (Department.class.equals(query.getResultType())) {
                root.fetch("parent", JoinType.LEFT);
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Join<Object, Object> members = root.join("members", JoinType.LEFT);
                Join<Object, Object> user = members.join("user", JoinType.LEFT);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern),
                        cb.and(
                                cb.isFalse(members.get("deleted")),
                                cb.or(
                                        cb.like(cb.lower(cb.coalesce(user.get("firstName"), "")), pattern),
                                        cb.like(cb.lower(cb.coalesce(user.get("lastName"), "")), pattern),
                                        cb.like(cb.lower(cb.coalesce(user.get("email"), "")), pattern),
                                        cb.like(cb.lower(cb.coalesce(members.get("designation"), "")), pattern)
                                )
                        )
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (StringUtils.hasText(parentUuid)) {
                Join<Object, Object> parent = root.join("parent", JoinType.INNER);
                predicates.add(cb.equal(parent.get("uuid"), java.util.UUID.fromString(parentUuid.trim())));
                predicates.add(cb.isFalse(parent.get("deleted")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
