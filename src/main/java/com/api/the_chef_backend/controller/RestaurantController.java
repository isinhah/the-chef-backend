package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.request.RestaurantRequestDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantResponseDTO;
import com.api.the_chef_backend.service.RestaurantService;
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
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable UUID id) {
        RestaurantResponseDTO dto = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<RestaurantResponseDTO> getAllRestaurants(@RequestParam(required = false) String name, @RequestParam(required = false) String phone, Pageable pageable) {
        return restaurantService.getAllRestaurants(name, phone, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantRequestDTO dto) {
        RestaurantResponseDTO newRestaurant = restaurantService.createRestaurant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRestaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> alterRestaurant(@PathVariable UUID id, @Valid @RequestBody RestaurantRequestDTO dto) {
        RestaurantResponseDTO existingRestaurant = restaurantService.alterRestaurant(id, dto);
        return ResponseEntity.ok(existingRestaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
