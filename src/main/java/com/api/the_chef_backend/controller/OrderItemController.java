package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.controller.swagger.OrderItemControllerDoc;
import com.api.the_chef_backend.model.dtos.request.OrderItemRequestDTO;
import com.api.the_chef_backend.model.dtos.response.OrderItemResponseDTO;
import com.api.the_chef_backend.service.OrderItemService;
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
@RequestMapping("/api/v1/restaurants/{restaurantId}/orders/{orderId}/items")
public class OrderItemController implements OrderItemControllerDoc {

    private final OrderItemService orderItemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> getOrderItemById(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        OrderItemResponseDTO dto = orderItemService.getOrderItemById(restaurantId, orderId, itemId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<OrderItemResponseDTO> getAllOrderItems(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId,
            Pageable pageable) {
        return orderItemService.getAllOrderItems(restaurantId, orderId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> createOrderItem(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDTO dto) {
        OrderItemResponseDTO newItem = orderItemService.createOrderItem(restaurantId, orderId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @Valid @RequestBody OrderItemRequestDTO dto) {
        OrderItemResponseDTO updatedItem = orderItemService.updateOrderItem(restaurantId, orderId, itemId, dto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable UUID restaurantId,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        orderItemService.deleteOrderItem(restaurantId, orderId, itemId);
        return ResponseEntity.noContent().build();
    }
}