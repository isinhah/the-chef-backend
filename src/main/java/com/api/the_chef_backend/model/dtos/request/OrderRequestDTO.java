package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record OrderRequestDTO(
        @NotNull(message = "O Id da mesa é obrigatório.")
        Long tableId,
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
        String waiter,
        @NotNull(message = "O Id do restaurante é obrigatório.")
        UUID restaurantId
) {
}