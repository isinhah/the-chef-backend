package com.api.the_chef_backend.model.dtos.auth;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RegisterRestaurantDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name,
        @NotBlank(message = "CPF ou CNPJ é obrigatório.")
        @Size(max = 14, message = "CPF/CNPJ deve ter no máximo 14 caracteres.")
        String cpfOrCnpj,
        String phone,
        @Email(message = "O e-mail deve ser válido.")
        String email,
        @Size(min = 6, max = 10, message = "A senha deve ter no mínimo 6 caracteres e no máximo 10 caracteres.")
        String password,
        @Positive(message = "A quantidade de mesas deve ser um número positivo.")
        int tableQuantity,
        @DecimalMin(value = "0.0", message = "A comissão do garçom não pode ser negativa.")
        BigDecimal waiterCommission
) {
}
