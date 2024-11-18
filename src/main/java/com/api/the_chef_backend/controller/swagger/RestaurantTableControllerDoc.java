package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.request.RestaurantTableRequestDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantTableResponseDTO;
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

@Tag(name = "restaurant tables controller")
public interface RestaurantTableControllerDoc {

    @Operation(summary = "get table by id", description = "retrieve a specific table by its id in a restaurant")
    @ApiResponse(responseCode = "200", description = "table retrieved successfully")
    ResponseEntity<RestaurantTableResponseDTO> getTableById(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "table id") @PathVariable Long tableId
    );

    @Operation(summary = "get all tables", description = "retrieve a list of all tables for a specific restaurant")
    @ApiResponse(responseCode = "200", description = "tables retrieved successfully")
    List<RestaurantTableResponseDTO> getAllTablesByRestaurant(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "create table", description = "create a new table for a specific restaurant")
    @ApiResponse(responseCode = "201", description = "table created successfully")
    ResponseEntity<RestaurantTableResponseDTO> createTable(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @RequestBody RestaurantTableRequestDTO dto
    );

    @Operation(summary = "update table", description = "modify details of a specific table in a restaurant")
    @ApiResponse(responseCode = "200", description = "table updated successfully")
    ResponseEntity<RestaurantTableResponseDTO> alterTable(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "table id") @PathVariable Long tableId,
            @RequestBody RestaurantTableRequestDTO dto
    );

    @Operation(summary = "delete table", description = "remove a specific table from a restaurant by its id")
    @ApiResponse(responseCode = "204", description = "table deleted successfully")
    ResponseEntity<Void> deleteTable(
            @Parameter(description = "table id") @PathVariable Long tableId
    );
}
