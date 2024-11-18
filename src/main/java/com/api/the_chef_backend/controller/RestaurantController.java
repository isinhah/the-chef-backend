package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.controller.swagger.RestaurantControllerDoc;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantResponseDTO;
import com.api.the_chef_backend.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController implements RestaurantControllerDoc {

    private final RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable UUID id) {
        RestaurantResponseDTO dto = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<RestaurantResponseDTO> getAllRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            Pageable pageable) {
        return restaurantService.getAllRestaurants(name, phone, pageable).getContent();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> alterRestaurant(
            @PathVariable UUID id,
            @Valid @RequestBody RegisterRestaurantDTO dto) {
        RestaurantResponseDTO existingRestaurant = restaurantService.alterRestaurant(id, dto);
        return ResponseEntity.ok(existingRestaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
