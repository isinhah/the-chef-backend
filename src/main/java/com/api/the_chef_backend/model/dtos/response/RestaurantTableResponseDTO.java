package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.RestaurantTable;

import java.util.UUID;

public record RestaurantTableResponseDTO(
        Long id,
        String name,
        int tableNumber,
        UUID restaurantId
) {
    public RestaurantTableResponseDTO(RestaurantTable restaurantTable) {
        this(restaurantTable.getId(), restaurantTable.getName(), restaurantTable.getTableNumber(), restaurantTable.getRestaurant().getId());
    }
}