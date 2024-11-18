package com.api.the_chef_backend.model.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "restaurant table request dto", description = "dto para a criação de uma mesa no restaurante")
public record RestaurantTableRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 10, message = "O nome deve ter no máximo 10 caracteres.")
        @Schema(description = "Nome da mesa", example = "Mesa 1")
        String name,

        @Min(value = 0, message = "O número da mesa não pode ser negativo.")
        @Schema(description = "Número da mesa", example = "1")
        int tableNumber
) {
}
