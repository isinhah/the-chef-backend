package com.api.the_chef_backend.model.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(name = "product request dto", description = "dto para criar o produto")
public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Schema(description = "Nome do produto", example = "Sushi de Salmão")
        String name,

        @Schema(description = "URL da imagem do produto", example = "http://example.com/images/sushi.jpg")
        String imageUrl,

        @Schema(description = "Descrição do produto", example = "Sushi de salmão fresco com arroz e alga.")
        String description,

        @NotNull(message = "O preço do produto é obrigatório.")
        @Min(value = 0, message = "O preço do produto não pode ser negativo.")
        @Schema(description = "Preço do produto", example = "29.99")
        BigDecimal price,

        @Size(max = 10, message = "O código pdv deve ter no máximo 10 caracteres.")
        @Schema(description = "Código PDV do produto", example = "1234567890")
        String pdvCode,

        @Min(value = 0, message = "O estoque do produto não pode ser negativo.")
        @Schema(description = "Quantidade em estoque do produto", example = "50")
        int stock,

        @NotNull(message = "O id de categoria é obrigatório.")
        @Schema(description = "ID da categoria do produto", example = "1")
        Long categoryId
) {
}