package com.api.the_chef_backend.specification;

import com.api.the_chef_backend.model.entity.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CategorySpecification {

    public static Specification<Category> withFilters(String name, UUID restaurantId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("name")), "%" +
                                name.toLowerCase() + "%"
                        )
                );
            }

            if (restaurantId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.get("restaurantId").get("id"), restaurantId)
                );
            }

            return predicate;
        };
    }
}
