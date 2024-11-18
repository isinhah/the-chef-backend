package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Schema(name = "order response dto", description = "dto de resposta para um pedido")
public record OrderResponseDTO(
        @Schema(description = "Identificador único do pedido", example = "1")
        Long id,

        @Schema(description = "Identificador único do restaurante associado ao pedido", example = "uuid-1234-abcd")
        UUID restaurantId,

        @Schema(description = "Identificador único da mesa associada ao pedido", example = "10")
        Long tableId,

        @Schema(description = "Nome do garçom responsável pelo pedido", example = "João Silva")
        String waiter,

        @Schema(description = "Hora de início do pedido", example = "2024-11-18T12:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startTime,

        @Schema(description = "Valor total do pedido", example = "150.50")
        BigDecimal total,

        @Schema(description = "Lista de itens do pedido", implementation = OrderItemResponseDTO.class)
        Set<OrderItemResponseDTO> items
) {
        public OrderResponseDTO(Order order) {
                this(order.getId(),
                        order.getRestaurant().getId(),
                        order.getTable().getId(),
                        order.getWaiter(),
                        order.getStartTime(),
                        order.getTotal(),
                        order.getItems() != null ? order.getItems().stream()
                                .map(OrderItemResponseDTO::new)
                                .collect(Collectors.toSet()) : Collections.emptySet()
                );
        }
}