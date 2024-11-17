package com.api.the_chef_backend.model.repository;

import com.api.the_chef_backend.model.entity.RestaurantTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Page<RestaurantTable> findAllByRestaurantId(UUID restaurantId, Pageable pageable);
    boolean existsByRestaurantIdAndTableNumber(UUID restaurantId, int tableNumber);
}
