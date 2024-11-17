package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.OrderItem;
import com.api.the_chef_backend.model.entity.ProductExtra;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderItemResponseDTO(
        Long id,
        Long orderId,
        Long productId,
        int productQuantity,
        int complementsQuantity,
        Set<Long> complementsId
) {

    public OrderItemResponseDTO(OrderItem orderItem) {
        this(orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getProductQuantity(),
                verifyComplementsQuantity(orderItem),
                orderItem.getComplements() == null ? Collections.emptySet() : orderItem.getComplements().stream()
                        .map(ProductExtra::getId)
                        .collect(Collectors.toSet())
        );
    }

    private static int verifyComplementsQuantity(OrderItem orderItem) {
        Set<ProductExtra> complements = orderItem.getComplements();
        return (complements == null || complements.isEmpty()) ? 0 : complements.size();
    }

}