package com.api.the_chef_backend.model.dtos.request;

import java.util.UUID;

public record RestaurantTableRequestDTO(
        String name,
        int tableNumber,
        UUID restaurantId
) {
}