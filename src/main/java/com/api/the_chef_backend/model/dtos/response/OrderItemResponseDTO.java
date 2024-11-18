package com.api.the_chef_backend.model.dtos.response;

import com.api.the_chef_backend.model.entity.OrderItem;
import com.api.the_chef_backend.model.entity.ProductExtra;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(name = "order item response dto", description = "dto de resposta para um item de pedido")
public record OrderItemResponseDTO(
        @Schema(description = "Identificador único do item de pedido", example = "1")
        Long id,

        @Schema(description = "Identificador único do pedido associado", example = "1001")
        Long orderId,

        @Schema(description = "Identificador único do produto", example = "200")
        Long productId,

        @Schema(description = "Quantidade do produto no item do pedido", example = "3")
        int productQuantity,

        @Schema(description = "Quantidade de complementos para o item do pedido", example = "2")
        int complementsQuantity,

        @Schema(description = "Conjunto de identificadores dos complementos associados ao item do pedido", example = "[1, 2]")
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