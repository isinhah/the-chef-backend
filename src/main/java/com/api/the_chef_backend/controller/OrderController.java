package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.controller.swagger.OrderControllerDoc;
import com.api.the_chef_backend.model.dtos.request.OrderRequestDTO;
import com.api.the_chef_backend.model.dtos.response.OrderResponseDTO;
import com.api.the_chef_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/orders")
public class OrderController implements OrderControllerDoc {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId) {
        OrderResponseDTO dto = orderService.getOrderById(restaurantId, orderId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<OrderResponseDTO> getAllOrders(
            @RequestParam(required = false) Long tableId,
            @PathVariable UUID restaurantId,
            Pageable pageable) {
        return orderService.getAllOrders(tableId, restaurantId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO newOrder = orderService.createOrder(restaurantId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> alterOrder(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO existingOrder = orderService.alterOrder(restaurantId, orderId, dto);
        return ResponseEntity.ok(existingOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId) {
        orderService.deleteOrder(restaurantId, orderId);
        return ResponseEntity.noContent().build();
    }
}