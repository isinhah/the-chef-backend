package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.request.ProductExtraRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductExtraResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "complements controller")
public interface ProductExtraControllerDoc {

    @Operation(summary = "get complement by id", description = "retrieve a specific product complement by its id")
    @ApiResponse(responseCode = "200", description = "complement retrieved successfully")
    ResponseEntity<ProductExtraResponseDTO> getComplementById(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "complement id") @PathVariable Long complementId
    );

    @Operation(summary = "get all complements", description = "retrieve a list of all complements for a specific restaurant")
    @ApiResponse(responseCode = "200", description = "complements retrieved successfully")
    List<ProductExtraResponseDTO> getAllComplements(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "pagination information") Pageable pageable
    );

    @Operation(summary = "create a new complement", description = "add a new complement to a specific restaurant's product")
    @ApiResponse(responseCode = "201", description = "complement created successfully")
    ResponseEntity<ProductExtraResponseDTO> createComplement(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @RequestBody ProductExtraRequestDTO dto
    );

    @Operation(summary = "update a complement", description = "modify an existing complement for a specific restaurant")
    @ApiResponse(responseCode = "200", description = "complement updated successfully")
    ResponseEntity<ProductExtraResponseDTO> alterComplement(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "complement id") @PathVariable Long complementId,
            @RequestBody ProductExtraRequestDTO dto
    );

    @Operation(summary = "delete a complement", description = "remove a specific complement from a restaurant's product")
    @ApiResponse(responseCode = "204", description = "complement deleted successfully")
    ResponseEntity<Void> deleteComplement(
            @Parameter(description = "restaurant id") @PathVariable UUID restaurantId,
            @Parameter(description = "complement id") @PathVariable Long complementId
    );
}
