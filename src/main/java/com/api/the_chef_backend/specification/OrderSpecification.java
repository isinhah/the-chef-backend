package com.api.the_chef_backend.specification;

import com.api.the_chef_backend.model.entity.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class OrderSpecification {

    public static Specification<Order> withFilters(Long tableId, UUID restaurantId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (tableId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.get("table").get("id"), tableId)
                );
            }

            if (restaurantId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.get("restaurant").get("id"), restaurantId)
                );
            }

            return predicate;
        };
    }
}
