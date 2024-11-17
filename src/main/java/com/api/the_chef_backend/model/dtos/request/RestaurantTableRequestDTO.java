package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantTableRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 10, message = "O nome deve ter no máximo 10 caracteres.")
        String name,
        @Min(value = 0, message = "O número da mesa não pode ser negativo.")
        int tableNumber
) {
}