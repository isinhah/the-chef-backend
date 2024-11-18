package com.api.the_chef_backend.model.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "order request dto", description = "dto para a criação do pedido")
public record OrderRequestDTO(
        @NotNull(message = "O Id da mesa é obrigatório.")
        @Schema(description = "ID da mesa associada ao pedido", example = "101")
        Long tableId,

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
        @Schema(description = "Nome do garçom responsável pelo pedido", example = "João")
        String waiter
) {
}