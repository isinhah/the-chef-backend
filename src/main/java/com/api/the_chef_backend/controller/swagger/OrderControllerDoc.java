package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.request.OrderRequestDTO;
import com.api.the_chef_backend.model.dtos.response.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "order controller")
public interface OrderControllerDoc {
    @Operation(summary = "get order by id", description = "retrieve a specific order by its id")
    @ApiResponse(responseCode = "200", description = "order found successfully")
    ResponseEntity<OrderResponseDTO> getOrderById(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId
    );

    @Operation(summary = "get all orders", description = "list all orders with optional table filter")
    @ApiResponse(responseCode = "200", description = "orders retrieved successfully")
    List<OrderResponseDTO> getAllOrders(
            @Parameter(description = "filter by table id") @RequestParam(required = false) Long tableId,
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "create a new order", description = "add a new order to the restaurant")
    @ApiResponse(responseCode = "201", description = "order created successfully")
    ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @RequestBody OrderRequestDTO dto
    );

    @Operation(summary = "update an existing order", description = "update the details of an existing order")
    @ApiResponse(responseCode = "200", description = "order updated successfully")
    ResponseEntity<OrderResponseDTO> alterOrder(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId,
            @RequestBody OrderRequestDTO dto
    );

    @Operation(summary = "delete an order", description = "remove an order from the restaurant")
    @ApiResponse(responseCode = "204", description = "order deleted successfully")
    ResponseEntity<Void> deleteOrder(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "order id") @PathVariable Long orderId
    );
}
