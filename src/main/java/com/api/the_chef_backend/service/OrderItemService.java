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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductExtraRepository productExtraRepository;

    public OrderItemResponseDTO getOrderItemById(UUID restaurantId, Long orderId, Long orderItemId) {
        verifyRestaurantIdExists(restaurantId);
        verifyOrderIdExists(orderId);

        OrderItem item = verifyOrderItemIdExists(orderItemId);

        if (!item.getOrder().getId().equals(orderId) || !item.getOrder().getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Item do pedido não foi encontrado para o pedido e restaurante especificado.");
        }

        return new OrderItemResponseDTO(item);
    }

    public Page<OrderItemResponseDTO> getAllOrderItems(UUID restaurantId, Long orderId, Pageable pageable) {
        verifyRestaurantIdExists(restaurantId);

        Order order = verifyOrderIdExists(orderId);
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Pedido não foi encontrado no restaurante especificado.");
        }

        Page<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId, pageable);
        return orderItems.map(OrderItemResponseDTO::new);
    }

    @Transactional
    public OrderItemResponseDTO createOrderItem(UUID restaurantId, Long orderId, OrderItemRequestDTO dto) {
        verifyRestaurantIdExists(restaurantId);

        Order order = verifyOrderIdExists(orderId);
        if (!order.getRestaurant().getId().equals(restaurantId)) {
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

        return new OrderItemResponseDTO(item);
    }

    @Transactional
    public OrderItemResponseDTO updateOrderItem(UUID restaurantId, Long orderId, Long itemId, OrderItemRequestDTO dto) {
        verifyRestaurantIdExists(restaurantId);
        Order order = verifyOrderIdExists(orderId);

        OrderItem existingItem = verifyOrderItemIdExists(itemId);
        if (!existingItem.getOrder().getId().equals(orderId) || !existingItem.getOrder().getRestaurant().getId().equals(restaurantId)) {
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

        return new OrderItemResponseDTO(existingItem);
    }

    @Transactional
    public void deleteOrderItem(UUID restaurantId, Long orderId, Long itemId) {
        verifyRestaurantIdExists(restaurantId);
        verifyOrderIdExists(orderId);

        OrderItem existingItem = verifyOrderItemIdExists(itemId);
        if (!existingItem.getOrder().getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Item do pedido não foi encontrado no restaurante especificado.");
        }

        orderItemRepository.deleteById(itemId);
    }

    private OrderItem verifyOrderItemIdExists(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("O item do pedido não foi encontrado com esse id."));
    }

    private Order verifyOrderIdExists(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com esse id."));
    }

    private Product verifyProductIdExists(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com esse id."));
    }

    private void verifyRestaurantIdExists(UUID id) {
        restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private ProductExtra verifyComplementIdExists(Long id) {
        return productExtraRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Complemento não encontrado com esse id."));
    }
}
