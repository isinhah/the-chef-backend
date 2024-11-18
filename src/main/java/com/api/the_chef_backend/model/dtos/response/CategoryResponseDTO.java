package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "category response dto", description = "dto de resposta para categoria")
public record CategoryResponseDTO(
        @Schema(description = "Identificador único da categoria", example = "1")
        Long id,

        @Schema(description = "Nome da categoria", example = "Sushis")
        String name,

        @Schema(description = "Identificador único do restaurante associado", example = "b19ff8c7-74c5-4ff6-963d-28a56a2c56ff")
        UUID restaurantId
) {
    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getRestaurant().getId());
    }
}