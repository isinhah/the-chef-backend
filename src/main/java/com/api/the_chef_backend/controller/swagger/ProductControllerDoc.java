package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.request.ProductRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductResponseDTO;
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

@Tag(name = "product controller")
public interface ProductControllerDoc {

    @Operation(summary = "get product by id", description = "retrieve a specific product by its id")
    @ApiResponse(responseCode = "200", description = "product retrieved successfully")
    ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "product id") @PathVariable Long productId
    );

    @Operation(summary = "get all products", description = "retrieve a list of all products for a specific restaurant")
    @ApiResponse(responseCode = "200", description = "products retrieved successfully")
    List<ProductResponseDTO> getAllProducts(
            @Parameter(description = "optional product name for filtering") @RequestParam(required = false) String name,
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "create a new product", description = "add a new product to a specific restaurant's menu")
    @ApiResponse(responseCode = "201", description = "product created successfully")
    ResponseEntity<ProductResponseDTO> createProduct(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @RequestBody ProductRequestDTO dto
    );

    @Operation(summary = "update a product", description = "modify an existing product for a specific restaurant")
    @ApiResponse(responseCode = "200", description = "product updated successfully")
    ResponseEntity<ProductResponseDTO> alterProduct(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "product id") @PathVariable Long productId,
            @RequestBody ProductRequestDTO dto
    );

    @Operation(summary = "delete a product", description = "remove a specific product from a restaurant's menu")
    @ApiResponse(responseCode = "204", description = "product deleted successfully")
    ResponseEntity<Void> deleteProduct(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "product id") @PathVariable Long productId
    );
}
