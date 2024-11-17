package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String name,
        String imageUrl,
        String description,
        @NotNull(message = "O preço do produto é obrigatório.")
        @Min(value = 0, message = "A preço do produto não pode ser negativo.")
        BigDecimal price,
        @Size(max = 10, message = "O código pdv deve ter no máximo 10 caracteres.")
        String pdvCode,
        @Min(value = 0, message = "O estoque do produto não pode ser negativo.")
        int stock,
        @NotNull(message = "O id de categoria é obrigatório.")
        Long categoryId
) {
}