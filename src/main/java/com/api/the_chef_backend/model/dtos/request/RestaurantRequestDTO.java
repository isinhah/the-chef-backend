package com.api.the_chef_backend.model.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RestaurantRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name,
        @NotBlank(message = "CPF ou CNPJ é obrigatório.")
        @Size(max = 14, message = "CPF/CNPJ deve ter no máximo 14 caracteres.")
        String cpfOrCnpj,
        @NotBlank(message = "O telefone é obrigatório.")
        @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
        String phone,
        @Positive(message = "A quantidade de mesas deve ser um número positivo.")
        int tableQuantity,
        @DecimalMin(value = "0.0", message = "A comissão do garçom não pode ser negativa.")
        BigDecimal waiterCommission
) {
}