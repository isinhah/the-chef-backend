package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.ProductExtra;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductExtraResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        UUID restaurantId
) {
    public ProductExtraResponseDTO(ProductExtra complement) {
        this(complement.getId(), complement.getName(), complement.getPrice(), complement.getRestaurant().getId());
    }
}
