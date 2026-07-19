package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.FollowUp;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link FollowUp} search and filter queries.
 */
public final class FollowUpSpecification {

    private FollowUpSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for follow-up search/filter.
     *
     * @param search           free-text search
     * @param status           follow-up outcome status
     * @param type             follow-up type
     * @param clientUuid       client or lead UUID filter
     * @param assignedUserUuid assigned user UUID filter
     * @param followUpFrom     follow-up date from
     * @param followUpTo       follow-up date to
     * @return specification
     */
    public static Specification<FollowUp> filter(
            String search,
            FollowUpStatus status,
            FollowUpType type,
            UUID clientUuid,
            UUID assignedUserUuid,
            LocalDate followUpFrom,
            LocalDate followUpTo
    ) {
        return (root, query, cb) -> {
            if (FollowUp.class.equals(query.getResultType())) {
                root.fetch("client", JoinType.LEFT);
                root.fetch("lead", JoinType.LEFT);
                root.fetch("assignedUser", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("remark"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("clientName"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("client").get("email"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("lead").get("companyName"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("lead").get("email"), "")), pattern),
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
                        )
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("followUpStatus"), status));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("followUpType"), type));
            }
            if (clientUuid != null) {
                predicates.add(cb.or(
                        cb.equal(root.get("client").get("uuid"), clientUuid),
                        cb.equal(root.get("lead").get("uuid"), clientUuid)
                ));
            }
            if (assignedUserUuid != null) {
                predicates.add(cb.equal(root.get("assignedUser").get("uuid"), assignedUserUuid));
            }
            if (followUpFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("followUpDate"), followUpFrom));
            }
            if (followUpTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("followUpDate"), followUpTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
