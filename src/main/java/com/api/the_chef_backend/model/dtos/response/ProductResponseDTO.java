package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "product response dto", description = "dto de resposta para um produto")
public record ProductResponseDTO(
        @Schema(description = "Identificador único do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Sushi de Salmão")
        String name,

        @Schema(description = "URL da imagem do produto", example = "http://example.com/images/sushi.jpg")
        String imageUrl,

        @Schema(description = "Descrição do produto", example = "Sushi de salmão fresco com arroz e alga")
        String description,

        @Schema(description = "Preço do produto", example = "25.00")
        BigDecimal price,

        @Schema(description = "Código PDV do produto", example = "PDV-1234")
        String pdvCode,

        @Schema(description = "Identificador da categoria do produto", example = "2")
        Long categoryId,

        @Schema(description = "Identificador único do restaurante associado ao produto", example = "uuid-5678-efgh")
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
