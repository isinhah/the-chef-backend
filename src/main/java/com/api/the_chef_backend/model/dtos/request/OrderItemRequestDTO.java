package com.api.the_chef_backend.model.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(name = "order item request dto", description = "dto para a criação de um item de pedido")
public record OrderItemRequestDTO(
        @NotNull(message = "O id do produto é obrigatório.")
        @Schema(description = "ID do produto associado ao item do pedido", example = "12345")
        Long productId,

        @Min(value = 1, message = "A quantidade de unidades do produto deve ser no mínimo 1.")
        @Schema(description = "Quantidade de unidades do produto no item do pedido", example = "2")
        int productsQuantity,

        @Min(value = 0, message = "A quantidade de complementos não pode ser negativa.")
        @Max(value = 2, message = "A quantidade de complementos deve ser no máximo 2.")
        @Schema(description = "Quantidade de complementos no item do pedido (máximo de 2)", example = "1")
        int complementsQuantity,

        @Schema(description = "IDs dos complementos existentes que devem ser associados ao item do pedido", example = "[1, 2]")
        Set<Long> complementsIds
) {
}
