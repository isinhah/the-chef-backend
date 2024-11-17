package com.api.the_chef_backend.model.repository;

import com.api.the_chef_backend.model.entity.ProductExtra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductExtraRepository extends JpaRepository<ProductExtra, Long> {
    Page<ProductExtra> findAllByRestaurantId(UUID restaurantId, Pageable pageable);
    boolean existsByNameAndRestaurantId(String name, UUID restaurantId);
}