package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "restaurants controller")
public interface RestaurantControllerDoc {
    @Operation(summary = "get restaurant by id", description = "retrieve a specific restaurant by its id")
    @ApiResponse(responseCode = "200", description = "restaurant retrieved successfully")
    ResponseEntity<RestaurantResponseDTO> getRestaurantById(
            @Parameter(description = "restaurant id") @PathVariable UUID id
    );

    @Operation(summary = "get all restaurants", description = "retrieve a list of all restaurants")
    @ApiResponse(responseCode = "200", description = "restaurants retrieved successfully")
    List<RestaurantResponseDTO> getAllRestaurants(
            @Parameter(description = "restaurant name filter") String name,
            @Parameter(description = "restaurant phone filter") String phone,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "update restaurant", description = "modify details of a specific restaurant")
    @ApiResponse(responseCode = "200", description = "restaurant updated successfully")
    ResponseEntity<RestaurantResponseDTO> alterRestaurant(
            @Parameter(description = "restaurant id") @PathVariable UUID id,
            @RequestBody RegisterRestaurantDTO dto
    );

    @Operation(summary = "delete restaurant", description = "remove a specific restaurant by its id")
    @ApiResponse(responseCode = "204", description = "restaurant deleted successfully")
    ResponseEntity<Void> deleteRestaurant(
            @Parameter(description = "restaurant id") @PathVariable UUID id
    );
}
