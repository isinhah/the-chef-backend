package com.api.the_chef_backend.model.dtos.request;

import java.math.BigDecimal;
import java.util.UUID;

public record RestaurantRequestDTO(
        String name,
        String cpfOrCnpj,
        String phone,
        int tableQuantity,
        BigDecimal waiterCommission
) {
}