package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.ProductExtra;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "product extra response dto", description = "dto de resposta para um complemento do restaurante")
public record ProductExtraResponseDTO(
        @Schema(description = "Identificador único do complemento do produto", example = "1")
        Long id,

        @Schema(description = "Nome do complemento do produto", example = "Molho de Soja")
        String name,

        @Schema(description = "Preço do complemento do produto", example = "5.00")
        BigDecimal price,

        @Schema(description = "Identificador único do restaurante associado ao complemento do produto", example = "uuid-1234-abcd")
        UUID restaurantId
) {
    public ProductExtraResponseDTO(ProductExtra complement) {
        this(complement.getId(), complement.getName(), complement.getPrice(), complement.getRestaurant().getId());
    }
}
