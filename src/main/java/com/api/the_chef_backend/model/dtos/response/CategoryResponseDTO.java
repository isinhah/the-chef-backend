package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Category;

import java.util.UUID;

public record CategoryResponseDTO(
        Long id,
        String name,
        UUID restaurantId
) {
    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getRestaurant().getId());
    }
}