package com.api.the_chef_backend.model.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(name = "restaurant request dto", description = "dto para criar restaurante")
public record RegisterRestaurantDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        @Schema(description = "Nome do restaurante", example = "Sushi & Sashimi", maxLength = 100)
        String name,

        @NotBlank(message = "CPF ou CNPJ é obrigatório.")
        @Size(max = 14, message = "CPF/CNPJ deve ter no máximo 14 caracteres.")
        @Schema(description = "CPF ou CNPJ do restaurante", example = "12345678901234", maxLength = 14)
        String cpfOrCnpj,

        @Schema(description = "Telefone de contato do restaurante", example = "(11)98765-4321", maxLength = 15)
        String phone,

        @Email(message = "O e-mail deve ser válido.")
        @Schema(description = "E-mail de contato do restaurante", example = "contato@sushisashimi.com")
        String email,

        @Size(min = 6, max = 10, message = "A senha deve ter no mínimo 6 caracteres e no máximo 10 caracteres.")
        @Schema(description = "Senha de acesso para o restaurante", example = "senha123", minLength = 6, maxLength = 10)
        String password,

        @Positive(message = "A quantidade de mesas deve ser um número positivo.")
        @Schema(description = "Quantidade de mesas no restaurante", example = "10", minimum = "1")
        int tableQuantity,

        @DecimalMin(value = "0.0", message = "A comissão do garçom não pode ser negativa.")
        @Schema(description = "Comissão do garçom sobre as vendas", example = "15.00", minimum = "0.0")
        BigDecimal waiterCommission
) {
}
