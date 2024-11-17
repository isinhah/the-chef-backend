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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public OrderResponseDTO getOrderById(UUID restaurantId, Long orderId) {
        verifyRestaurantIdExists(restaurantId);
        Order order = verifyOrderIdExists(orderId);

        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Pedido não encontrado para o restaurante especificado.");
        }

        return new OrderResponseDTO(order);
    }

    public Page<OrderResponseDTO> getAllOrders(Long tableId, UUID restaurantId, Pageable pageable) {
        Specification<Order> specification = OrderSpecification.withFilters(tableId, restaurantId);
        Page<Order> ordersPage = orderRepository.findAll(specification, pageable);
        return ordersPage.map(OrderResponseDTO::new);
    }

    @Transactional
    public OrderResponseDTO createOrder(UUID restaurantId, OrderRequestDTO dto) {
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

        return new OrderResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO alterOrder(UUID restaurantId, Long orderId, OrderRequestDTO dto) {
        Order order = verifyOrderIdExists(orderId);
        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);
        RestaurantTable table = verifyTableIdExists(dto.tableId());

        if (!order.getRestaurant().getId().equals(restaurant.getId())) {
            throw new EntityNotFoundException("Pedido não encontrado para o restaurante especificado.");
        }

        order.setRestaurant(restaurant);
        order.setTable(table);
        order.setWaiter(dto.waiter());

        orderRepository.save(order);

        return new OrderResponseDTO(order);
    }

    @Transactional
    public void deleteOrder(UUID restaurantId, Long orderId) {
        Order order = verifyOrderIdExists(orderId);

        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Pedido não encontrado para o restaurante especificado.");
        }

        orderRepository.deleteById(orderId);
    }

    private Order verifyOrderIdExists(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private RestaurantTable verifyTableIdExists(Long id) {
        return restaurantTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Mesa não encontrado com esse id."));
    }
}
