package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.request.ProductExtraRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductExtraResponseDTO;
import com.api.the_chef_backend.service.ProductExtraService;
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
@RequestMapping("/api/v1/restaurants/{restaurantId}/complements")
public class ProductExtraController {

    private final ProductExtraService productExtraService;

    @GetMapping("/{complementId}")
    public ResponseEntity<ProductExtraResponseDTO> getComplementById(
            @PathVariable UUID restaurantId,
            @PathVariable Long complementId) {
        ProductExtraResponseDTO dto = productExtraService.getComplementById(restaurantId, complementId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<ProductExtraResponseDTO> getAllComplements(
            @PathVariable UUID restaurantId,
            Pageable pageable) {
        return productExtraService.getAllComplements(restaurantId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<ProductExtraResponseDTO> createComplement(
            @Valid @RequestBody ProductExtraRequestDTO dto) {
        ProductExtraResponseDTO newComplement = productExtraService.createComplement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComplement);
    }

    @PutMapping("/{complementId}")
    public ResponseEntity<ProductExtraResponseDTO> alterComplement(
            @PathVariable Long complementId,
            @Valid @RequestBody ProductExtraRequestDTO dto) {
        ProductExtraResponseDTO updatedComplement = productExtraService.alterComplement(complementId, dto);
        return ResponseEntity.ok(updatedComplement);
    }

    @DeleteMapping("/{complementId}")
    public ResponseEntity<Void> deleteComplement(
            @PathVariable UUID restaurantId,
            @PathVariable Long complementId) {
        productExtraService.deleteComplement(restaurantId, complementId);
        return ResponseEntity.noContent().build();
    }
}