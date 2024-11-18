package com.api.the_chef_backend.model.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "product extra request dto", description = "dto para criar os complementos do resturante")
public record ProductExtraRequestDTO(
        @NotBlank(message = "O nome do complemento é obrigatório.")
        @Schema(description = "Nome do complemento", example = "Molho de soja")
        String name,

        @NotNull(message = "O preço do complemento é obrigatório.")
        @Min(value = 0, message = "O preço do produto não pode ser negativo.")
        @Schema(description = "Preço do complemento", example = "5.99")
        BigDecimal price,

        @Min(value = 0, message = "O estoque do complemento não pode ser negativo.")
        @Schema(description = "Quantidade em estoque do complemento", example = "100")
        int stock
) {
}