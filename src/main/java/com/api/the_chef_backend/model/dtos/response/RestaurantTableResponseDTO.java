package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.RestaurantTable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "restaurant table response dto", description = "dto de resposta para informações de uma mesa de restaurante")
public record RestaurantTableResponseDTO(
        @Schema(description = "Identificador único da mesa", example = "1")
        Long id,

        @Schema(description = "Nome da mesa", example = "Mesa 1")
        String name,

        @Schema(description = "Número da mesa", example = "5")
        int tableNumber,

        @Schema(description = "Identificador do restaurante ao qual a mesa pertence", example = "uuid-1234-abcd")
        UUID restaurantId
) {
    public RestaurantTableResponseDTO(RestaurantTable restaurantTable) {
        this(restaurantTable.getId(), restaurantTable.getName(), restaurantTable.getTableNumber(), restaurantTable.getRestaurant().getId());
    }
}