package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.request.OrderItemRequestDTO;
import com.api.the_chef_backend.model.dtos.response.OrderItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "order item controller")
public interface OrderItemControllerDoc {

    @Operation(summary = "get order item by id", description = "retrieve a specific order item by its id")
    @ApiResponse(responseCode = "200", description = "order item found successfully")
    ResponseEntity<OrderItemResponseDTO> getOrderItemById(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId,
            @Parameter(description = "item id") @PathVariable Long itemId
    );

    @Operation(summary = "get all order items", description = "list all items of a specific order")
    @ApiResponse(responseCode = "200", description = "order items retrieved successfully")
    List<OrderItemResponseDTO> getAllOrderItems(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "create a new order item", description = "add a new item to a specific order")
    @ApiResponse(responseCode = "201", description = "order item created successfully")
    ResponseEntity<OrderItemResponseDTO> createOrderItem(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId,
            @RequestBody OrderItemRequestDTO dto
    );

    @Operation(summary = "update an existing order item", description = "update the details of a specific order item")
    @ApiResponse(responseCode = "200", description = "order item updated successfully")
    ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId,
            @Parameter(description = "item id") @PathVariable Long itemId,
            @RequestBody OrderItemRequestDTO dto
    );

    @Operation(summary = "delete an order item", description = "remove an item from a specific order")
    @ApiResponse(responseCode = "204", description = "order item deleted successfully")
    ResponseEntity<Void> deleteOrderItem(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId,
            @Parameter(description = "item id") @PathVariable Long itemId
    );
}
