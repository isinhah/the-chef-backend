package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Restaurant;

import java.math.BigDecimal;
import java.util.UUID;

public record RestaurantResponseDTO(
        UUID id,
        String name,
        String email,
        String cpfOrCnpj,
        String phone,
        int tableQuantity,
        BigDecimal waiterCommission
) {
    public RestaurantResponseDTO(Restaurant restaurant) {
        this(restaurant.getId(),  restaurant.getName(), restaurant.getEmail(), restaurant.getCpfCnpj(), restaurant.getPhone(), restaurant.getTableQuantity(), restaurant.getWaiterCommission());
    }
}