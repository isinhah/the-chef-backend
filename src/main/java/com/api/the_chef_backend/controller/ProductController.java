package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.request.ProductRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductResponseDTO;
import com.api.the_chef_backend.service.ProductService;
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
@RequestMapping("/api/v1/restaurants/{restaurantId}/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID restaurantId, @PathVariable Long productId) {
        ProductResponseDTO dto = productService.getProductById(restaurantId, productId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts(
            @RequestParam(required = false) String name,
            @PathVariable UUID restaurantId,
            Pageable pageable) {
        return productService.getAllProducts(name, restaurantId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO newProduct = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> alterProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO existingProduct = productService.alterProduct(productId, dto);
        return ResponseEntity.ok(existingProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID restaurantId,
            @PathVariable Long productId) {
        productService.deleteProduct(restaurantId, productId);
        return ResponseEntity.noContent().build();
    }
}