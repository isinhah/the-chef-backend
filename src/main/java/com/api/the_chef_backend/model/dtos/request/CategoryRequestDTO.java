package com.api.the_chef_backend.model.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "category request dto", description = "dto para a criação de uma categoria")
public record CategoryRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
        @Schema(description = "Nome da categoria, com no máximo 50 caracteres", example = "Sushi")
        String name
) {
}
