package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.request.CategoryRequestDTO;
import com.api.the_chef_backend.model.dtos.response.CategoryResponseDTO;
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

@Tag(name = "category controller")
public interface CategoryControllerDoc {

    @Operation(summary = "get category by id", description = "retrieve a specific category by its id")
    @ApiResponse(responseCode = "200", description = "category found successfully")
    ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "category id") @PathVariable Long categoryId
    );

    @Operation(summary = "get all categories", description = "list all categories with optional filters")
    @ApiResponse(responseCode = "200", description = "categories retrieved successfully")
    List<CategoryResponseDTO> getAllCategories(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "filter by name") @RequestParam(required = false) String name,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "create a new category", description = "add a new category to the restaurant")
    @ApiResponse(responseCode = "201", description = "category created successfully")
    ResponseEntity<CategoryResponseDTO> createCategory(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @RequestBody CategoryRequestDTO dto
    );

    @Operation(summary = "update an existing category", description = "update the details of an existing category")
    @ApiResponse(responseCode = "200", description = "category updated successfully")
    ResponseEntity<CategoryResponseDTO> alterCategory(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "category id") @PathVariable Long categoryId,
            @RequestBody CategoryRequestDTO dto
    );

    @Operation(summary = "delete a category", description = "remove a category from the restaurant")
    @ApiResponse(responseCode = "204", description = "category deleted successfully")
    ResponseEntity<Void> deleteCategory(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "category id") @PathVariable Long categoryId
    );
}