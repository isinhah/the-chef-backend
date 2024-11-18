package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "authentication controller")
public interface AuthenticationControllerDoc {

    @Operation(summary = "create a new restaurant")
    @ApiResponse(responseCode = "201", description = "restaurant created successfully")
    ResponseEntity<AuthResponseDTO> registerRestaurant(@RequestBody RegisterRestaurantDTO dto);

}