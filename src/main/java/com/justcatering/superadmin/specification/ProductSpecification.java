package com.justcatering.superadmin.specification;

import com.justcatering.superadmin.entity.Product;
import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * JPA Specifications for {@link Product} search and filter queries.
 */
public final class ProductSpecification {

    private ProductSpecification() {
        // Utility class
    }

    /**
     * Builds a specification for product search/filter.
     *
     * @param search      free-text across name/code/type/description
     * @param productType optional type filter
     * @param status      optional status filter
     * @return specification
     */
    public static Specification<Product> filter(String search, String productType, EntityStatus status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(root.get("productType")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern)
                ));
            }

            if (StringUtils.hasText(productType)) {
                predicates.add(cb.equal(cb.lower(root.get("productType")), productType.trim().toLowerCase()));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
