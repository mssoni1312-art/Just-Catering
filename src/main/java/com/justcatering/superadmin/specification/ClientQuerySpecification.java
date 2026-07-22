package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.ClientQuery;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link ClientQuery} search and filter queries.
 */
public final class ClientQuerySpecification {

    private ClientQuerySpecification() {
        // Utility class
    }

    /**
     * Builds a specification for client query search/filter.
     *
     * @param search           free-text search
     * @param queryStatus      workflow status filter
     * @param priority         priority filter
     * @param clientUuid       client UUID filter
     * @param assignedUserUuid assigned user UUID filter
     * @param departmentUuid   department UUID filter
     * @param queryType        query type filter
     * @return specification
     */
    public static Specification<ClientQuery> filter(
            String search,
            QueryStatus queryStatus,
            Priority priority,
            UUID clientUuid,
            UUID assignedUserUuid,
            UUID departmentUuid,
            String queryType
    ) {
        return (root, query, cb) -> {
            if (ClientQuery.class.equals(query.getResultType())) {
                var client = root.fetch("client", JoinType.LEFT);
                client.fetch("product", JoinType.LEFT);
                root.fetch("assignedUser", JoinType.LEFT);
                root.fetch("department", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("queryType"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("remarks"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("documentName"), "")), pattern),
                        cb.like(cb.lower(root.get("client").get("clientName")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("email"), "")), pattern),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("assignedUser").get("firstName"), "")),
                                pattern
                        ),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("assignedUser").get("lastName"), "")),
                                pattern
                        ),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("assignedUser").get("email"), "")),
                                pattern
                        ),
                        cb.like(
                                cb.lower(cb.coalesce(root.get("department").get("name"), "")),
                                pattern
                        )
                ));
            }

            if (queryStatus != null) {
                predicates.add(cb.equal(root.get("queryStatus"), queryStatus));
            }
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            if (clientUuid != null) {
                predicates.add(cb.equal(root.get("client").get("uuid"), clientUuid));
            }
            if (assignedUserUuid != null) {
                predicates.add(cb.equal(root.get("assignedUser").get("uuid"), assignedUserUuid));
            }
            if (departmentUuid != null) {
                predicates.add(cb.equal(root.get("department").get("uuid"), departmentUuid));
            }
            if (StringUtils.hasText(queryType)) {
                predicates.add(cb.equal(cb.lower(root.get("queryType")), queryType.trim().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
