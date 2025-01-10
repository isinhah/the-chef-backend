package com.api.the_chef_backend.service;

import com.api.the_chef_backend.model.dtos.request.OrderItemRequestDTO;
import com.api.the_chef_backend.model.dtos.response.OrderItemResponseDTO;
import com.api.the_chef_backend.model.entity.Order;
import com.api.the_chef_backend.model.entity.OrderItem;
import com.api.the_chef_backend.model.entity.Product;
import com.api.the_chef_backend.model.entity.ProductExtra;
import com.api.the_chef_backend.model.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductExtraRepository productExtraRepository;

    public OrderItemResponseDTO getOrderItemById(UUID restaurantId, Long orderId, Long orderItemId) {
        log.info("[OrderItemService.getOrderItemById] Begin - restaurantId: {}, orderId: {}, orderItemId: {}", restaurantId, orderId, orderItemId);

        verifyRestaurantIdExists(restaurantId);
        verifyOrderIdExists(orderId);

        OrderItem item = verifyOrderItemIdExists(orderItemId);

        if (!item.getOrder().getId().equals(orderId) || !item.getOrder().getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderItemService.getOrderItemById] End - Item not found for order and restaurant");
            throw new EntityNotFoundException("Item do pedido não foi encontrado para o pedido e restaurante especificado.");
        }

        log.info("[OrderItemService.getOrderItemById] End - Item found: {}", item);
        return new OrderItemResponseDTO(item);
    }

    public Page<OrderItemResponseDTO> getAllOrderItems(UUID restaurantId, Long orderId, Pageable pageable) {
        log.info("[OrderItemService.getAllOrderItems] Begin - restaurantId: {}, orderId: {}", restaurantId, orderId);

        verifyRestaurantIdExists(restaurantId);

        Order order = verifyOrderIdExists(orderId);
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderItemService.getAllOrderItems] End - Order not found for restaurant");
            throw new EntityNotFoundException("Pedido não foi encontrado no restaurante especificado.");
        }

        Page<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId, pageable);
        log.info("[OrderItemService.getAllOrderItems] End - Retrieved {} order items", orderItems.getSize());
        return orderItems.map(OrderItemResponseDTO::new);
    }

    @Transactional
    public OrderItemResponseDTO createOrderItem(UUID restaurantId, Long orderId, OrderItemRequestDTO dto) {
        log.info("[OrderItemService.createOrderItem] Begin - restaurantId: {}, orderId: {}, request: {}", restaurantId, orderId, dto);

        verifyRestaurantIdExists(restaurantId);

        Order order = verifyOrderIdExists(orderId);
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderItemService.createOrderItem] End - Order does not belong to specified restaurant");
            throw new EntityNotFoundException("Pedido não pertence ao restaurante especificado.");
        }

        Product product = verifyProductIdExists(dto.productId());

        Set<ProductExtra> complements = dto.complementsIds().stream()
                .map(id -> verifyComplementIdExists(id))
                .collect(Collectors.toSet());

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .productQuantity(dto.productsQuantity())
                .complementsQuantity(dto.complementsQuantity())
                .complements(complements)
                .build();

        orderItemRepository.save(item);
        order.calculateTotalPrice();

        log.info("[OrderItemService.createOrderItem] End - OrderItem created: {}", item);
        return new OrderItemResponseDTO(item);
    }

    @Transactional
    public OrderItemResponseDTO updateOrderItem(UUID restaurantId, Long orderId, Long itemId, OrderItemRequestDTO dto) {
        log.info("[OrderItemService.updateOrderItem] Begin - restaurantId: {}, orderId: {}, itemId: {}, request: {}", restaurantId, orderId, itemId, dto);

        verifyRestaurantIdExists(restaurantId);
        Order order = verifyOrderIdExists(orderId);

        OrderItem existingItem = verifyOrderItemIdExists(itemId);
        if (!existingItem.getOrder().getId().equals(orderId) || !existingItem.getOrder().getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderItemService.updateOrderItem] End - Item not found for order and restaurant");
            throw new EntityNotFoundException("Item do pedido não foi encontrado para o pedido e restaurante especificado.");
        }

        Product product = verifyProductIdExists(dto.productId());

        Set<ProductExtra> complements = dto.complementsIds().stream()
                .map(id -> verifyComplementIdExists(id))
                .collect(Collectors.toSet());

        existingItem.setProduct(product);
        existingItem.setComplements(complements);
        existingItem.setProductQuantity(dto.productsQuantity());
        existingItem.setComplementsQuantity(dto.complementsQuantity());

        orderItemRepository.save(existingItem);
        order.calculateTotalPrice();

        log.info("[OrderItemService.updateOrderItem] End - OrderItem updated: {}", existingItem);
        return new OrderItemResponseDTO(existingItem);
    }

    @Transactional
    public void deleteOrderItem(UUID restaurantId, Long orderId, Long itemId) {
        log.info("[OrderItemService.deleteOrderItem] Begin - restaurantId: {}, orderId: {}, itemId: {}", restaurantId, orderId, itemId);

        verifyRestaurantIdExists(restaurantId);
        verifyOrderIdExists(orderId);

        OrderItem existingItem = verifyOrderItemIdExists(itemId);
        if (!existingItem.getOrder().getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderItemService.deleteOrderItem] End - Item not found for restaurant");
            throw new EntityNotFoundException("Item do pedido não foi encontrado no restaurante especificado.");
        }

        orderItemRepository.deleteById(itemId);
        log.info("[OrderItemService.deleteOrderItem] End - OrderItem deleted with id: {}", itemId);
    }

    private OrderItem verifyOrderItemIdExists(Long id) {
        log.info("[OrderItemService.verifyOrderItemIdExists] Checking if order item exists with id: {}", id);
        return orderItemRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderItemService.verifyOrderItemIdExists] OrderItem not found with id: {}", id);
            return new EntityNotFoundException("Item do pedido não foi encontrado com esse id.");
        });
    }

    private Order verifyOrderIdExists(Long id) {
        log.info("[OrderItemService.verifyOrderIdExists] Checking if order exists with id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderItemService.verifyOrderIdExists] Order not found with id: {}", id);
            return new EntityNotFoundException("Pedido não encontrado com esse id.");
        });
    }

    private Product verifyProductIdExists(Long id) {
        log.info("[OrderItemService.verifyProductIdExists] Checking if product exists with id: {}", id);
        return productRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderItemService.verifyProductIdExists] Product not found with id: {}", id);
            return new EntityNotFoundException("Produto não encontrado com esse id.");
        });
    }

    private void verifyRestaurantIdExists(UUID id) {
        log.info("[OrderItemService.verifyRestaurantIdExists] Checking if restaurant exists with id: {}", id);
        restaurantRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderItemService.verifyRestaurantIdExists] Restaurant not found with id: {}", id);
            return new EntityNotFoundException("Restaurante não encontrado com esse id.");
        });
    }

    private ProductExtra verifyComplementIdExists(Long id) {
        log.info("[OrderItemService.verifyComplementIdExists] Checking if product extra exists with id: {}", id);
        return productExtraRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderItemService.verifyComplementIdExists] Complement not found with id: {}", id);
            return new EntityNotFoundException("Complemento não encontrado com esse id.");
        });
    }
}
