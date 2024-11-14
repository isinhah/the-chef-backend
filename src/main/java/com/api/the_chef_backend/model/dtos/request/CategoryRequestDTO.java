package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 50 caracteres.")
        String name,
        @NotNull(message = "O id do restaurante é obrigatório.")
        UUID restaurantId
) {
}