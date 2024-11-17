package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductExtraRequestDTO(
        @NotBlank(message = "O nome do complemento é obrigatório.")
        String name,
        @NotNull(message = "O preço do complemento é obrigatório.")
        @Min(value = 0, message = "A preço do produto não pode ser negativo.")
        BigDecimal price,
        @Min(value = 0, message = "O estoque do complemento não pode ser negativo.")
        int stock,
        @NotNull(message = "O id do restaurante é obrigatório.")
        UUID restaurantId
) {
}