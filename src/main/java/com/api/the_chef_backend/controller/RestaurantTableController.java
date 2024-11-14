package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.request.RestaurantTableRequestDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantTableResponseDTO;
import com.api.the_chef_backend.service.RestaurantTableService;
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
@RequestMapping("/api/v1/restaurants/{restaurantId}/tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    @GetMapping("/{tableId}")
    public ResponseEntity<RestaurantTableResponseDTO> getTableById(@PathVariable UUID restaurantId, @PathVariable Long tableId) {
        RestaurantTableResponseDTO dto = restaurantTableService.getTableById(restaurantId, tableId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<RestaurantTableResponseDTO> getAllTablesByRestaurant(@PathVariable UUID restaurantId, Pageable pageable) {
        return restaurantTableService.getAllTables(restaurantId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<RestaurantTableResponseDTO> createTable(@Valid @RequestBody RestaurantTableRequestDTO dto) {
        RestaurantTableResponseDTO newTable = restaurantTableService.createTable(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTable);
    }

    @PutMapping("/{tableId}")
    public ResponseEntity<RestaurantTableResponseDTO> alterTable(@PathVariable Long tableId, @Valid @RequestBody RestaurantTableRequestDTO dto) {
        RestaurantTableResponseDTO existingTable = restaurantTableService.alterTable(tableId, dto);
        return ResponseEntity.ok(existingTable);
    }

    @DeleteMapping("/{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long tableId) {
        restaurantTableService.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }
}
