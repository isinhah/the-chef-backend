package com.api.the_chef_backend.service;

import com.api.the_chef_backend.model.dtos.request.OrderRequestDTO;
import com.api.the_chef_backend.model.dtos.response.OrderResponseDTO;
import com.api.the_chef_backend.model.entity.Order;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.entity.RestaurantTable;
import com.api.the_chef_backend.model.repository.OrderRepository;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.model.repository.RestaurantTableRepository;
import com.api.the_chef_backend.specification.OrderSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public OrderResponseDTO getOrderById(UUID restaurantId, Long orderId) {
        log.info("[OrderService.getOrderById] Begin - restaurantId: {}, orderId: {}", restaurantId, orderId);

        verifyRestaurantIdExists(restaurantId);
        Order order = verifyOrderIdExists(orderId);

        if (!order.getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderService.getOrderById] End - Order not found for restaurant");
            throw new EntityNotFoundException("Pedido não encontrado para o restaurante especificado.");
        }

        log.info("[OrderService.getOrderById] End - Order found: {}", order);
        return new OrderResponseDTO(order);
    }

    public Page<OrderResponseDTO> getAllOrders(Long tableId, UUID restaurantId, Pageable pageable) {
        log.info("[OrderService.getAllOrders] Begin - restaurantId: {}, tableId: {}", restaurantId, tableId);

        Specification<Order> specification = OrderSpecification.withFilters(tableId, restaurantId);
        Page<Order> ordersPage = orderRepository.findAll(specification, pageable);

        log.info("[OrderService.getAllOrders] End - Orders retrieved, size: {}", ordersPage.getSize());
        return ordersPage.map(OrderResponseDTO::new);
    }

    @Transactional
    public OrderResponseDTO createOrder(UUID restaurantId, OrderRequestDTO dto) {
        log.info("[OrderService.createOrder] Begin - restaurantId: {}, request: {}", restaurantId, dto);

        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);
        RestaurantTable table = verifyTableIdExists(dto.tableId());

        Order order = Order.builder()
                .table(table)
                .waiter(dto.waiter())
                .startTime(LocalDateTime.now())
                .restaurant(restaurant)
                .items(new HashSet<>())
                .build();

        orderRepository.save(order);

        log.info("[OrderService.createOrder] End - Order created: {}", order);
        return new OrderResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO alterOrder(UUID restaurantId, Long orderId, OrderRequestDTO dto) {
        log.info("[OrderService.alterOrder] Begin - restaurantId: {}, orderId: {}, request: {}", restaurantId, orderId, dto);

        Order order = verifyOrderIdExists(orderId);
        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);
        RestaurantTable table = verifyTableIdExists(dto.tableId());

        if (!order.getRestaurant().getId().equals(restaurant.getId())) {
            log.error("[OrderService.alterOrder] End - Order not found for restaurant");
            throw new EntityNotFoundException("Pedido não encontrado para o restaurante especificado.");
        }

        order.setRestaurant(restaurant);
        order.setTable(table);
        order.setWaiter(dto.waiter());

        orderRepository.save(order);

        log.info("[OrderService.alterOrder] End - Order updated: {}", order);
        return new OrderResponseDTO(order);
    }

    @Transactional
    public void deleteOrder(UUID restaurantId, Long orderId) {
        log.info("[OrderService.deleteOrder] Begin - restaurantId: {}, orderId: {}", restaurantId, orderId);

        Order order = verifyOrderIdExists(orderId);

        if (!order.getRestaurant().getId().equals(restaurantId)) {
            log.error("[OrderService.deleteOrder] End - Order not found for restaurant");
            throw new EntityNotFoundException("Pedido não encontrado para o restaurante especificado.");
        }

        orderRepository.deleteById(orderId);

        log.info("[OrderService.deleteOrder] End - Order deleted with id: {}", orderId);
    }

    private Order verifyOrderIdExists(Long id) {
        log.info("[OrderService.verifyOrderIdExists] Checking if order exists with id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderService.verifyOrderIdExists] Order not found with id: {}", id);
            return new EntityNotFoundException("Pedido não encontrado com esse id.");
        });
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        log.info("[OrderService.verifyRestaurantIdExists] Checking if restaurant exists with id: {}", id);
        return restaurantRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderService.verifyRestaurantIdExists] Restaurant not found with id: {}", id);
            return new EntityNotFoundException("Restaurante não encontrado com esse id.");
        });
    }

    private RestaurantTable verifyTableIdExists(Long id) {
        log.info("[OrderService.verifyTableIdExists] Checking if table exists with id: {}", id);
        return restaurantTableRepository.findById(id).orElseThrow(() -> {
            log.error("[OrderService.verifyTableIdExists] Table not found with id: {}", id);
            return new EntityNotFoundException("Mesa não encontrado com esse id.");
        });
    }
}
