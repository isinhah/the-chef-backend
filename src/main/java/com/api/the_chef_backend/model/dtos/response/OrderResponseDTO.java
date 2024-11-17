package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderResponseDTO(
        Long id,
        UUID restaurantId,
        Long tableId,
        String waiter,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startTime,
        BigDecimal total,
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