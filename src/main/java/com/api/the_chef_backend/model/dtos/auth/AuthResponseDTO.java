package com.api.the_chef_backend.model.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;


@Schema(name = "auth response dto", description = "dto que contém a resposta da autenticação")
public record AuthResponseDTO(
        @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Nome do restaurante", example = "Sushi & Sashimi")
        String name,

        @Schema(description = "E-mail do usuário", example = "contato@sushisashimi.com")
        String email
) {
}