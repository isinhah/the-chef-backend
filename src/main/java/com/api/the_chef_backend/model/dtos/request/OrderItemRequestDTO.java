package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record OrderItemRequestDTO(
        @NotNull(message = "O id do produto é obrigatório.")
        Long productId,
        @Min(value = 1, message = "A quantidade de unidades do produto deve ser no mínimo 1.")
        int productsQuantity,
        @Min(value = 0, message = "A quantidade de complementos não pode ser negativa.")
        @Max(value = 2, message = "A quantidade de complementos deve ser no máximo 2.")
        int complementsQuantity,
        Set<Long> complementsIds
) {
}