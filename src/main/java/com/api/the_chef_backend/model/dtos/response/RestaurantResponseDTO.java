package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "restaurant response dto", description = "dto de resposta para do restaurante")
public record RestaurantResponseDTO(
        @Schema(description = "Identificador único do restaurante", example = "uuid-1234-abcd")
        UUID id,

        @Schema(description = "Nome do restaurante", example = "Sushi & Sashimi")
        String name,

        @Schema(description = "E-mail de contato do restaurante", example = "contato@sushisashimi.com")
        String email,

        @Schema(description = "CPF ou CNPJ do restaurante", example = "12345678000195")
        String cpfOrCnpj,

        @Schema(description = "Número de telefone de contato do restaurante", example = "(11)91234-5678")
        String phone,

        @Schema(description = "Quantidade de mesas no restaurante", example = "20")
        int tableQuantity,

        @Schema(description = "Comissão do garçom sobre as vendas", example = "5.00")
        BigDecimal waiterCommission
) {
    public RestaurantResponseDTO(Restaurant restaurant) {
        this(restaurant.getId(),  restaurant.getName(), restaurant.getEmail(), restaurant.getCpfCnpj(), restaurant.getPhone(), restaurant.getTableQuantity(), restaurant.getWaiterCommission());
    }
}