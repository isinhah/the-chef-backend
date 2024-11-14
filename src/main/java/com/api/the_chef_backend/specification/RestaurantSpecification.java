package com.api.the_chef_backend.specification;

import com.api.the_chef_backend.model.entity.Restaurant;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecification {

    public static Specification<Restaurant> withFilters(String name, String phone) {
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

            if (phone != null && !phone.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("phone")), "%" +
                                        phone.toLowerCase() + "%"
                        )
                );
            }


            return predicate;
        };
    }
}
