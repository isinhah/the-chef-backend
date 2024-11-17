package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
        Long id,
        String name,
        String imageUrl,
        String description,
        BigDecimal price,
        String pdvCode,
        Long categoryId,
        UUID restaurantId
) {
    public ProductResponseDTO(Product product) {
        this(product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPrice(),
                product.getPdvCode(),
                product.getCategory().getId(),
                product.getRestaurant().getId());
    }
}
