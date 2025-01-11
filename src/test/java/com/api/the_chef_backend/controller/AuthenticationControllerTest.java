package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.service.AuthService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthenticationController authenticationController;

    public RegisterRestaurantDTO restaurantRequest() {
        return new RegisterRestaurantDTO(
                "Sushi & Sashimi",
                "12345678901234",
                "(11)98765-4321",
                "contato@sushisashimi.com",
                "senha123",
                10,
                new BigDecimal("15.00")
        );
    }

    @Test
    void testRegisterRestaurant_WhenSuccessful() {
        RegisterRestaurantDTO restaurantRequest = restaurantRequest();

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "Sushi & Sashimi",
                "restaurant@example.com"
        );

        when(authService.registerRestaurant(any(RegisterRestaurantDTO.class))).thenReturn(authResponseDTO);

        assertDoesNotThrow(() -> authenticationController.registerRestaurant(restaurantRequest));
        verify(authService, times(1)).registerRestaurant(any(RegisterRestaurantDTO.class));
    }

    @Test
    void testRegisterRestaurant_WhenValidationFails() {
        RegisterRestaurantDTO invalidDTO = new RegisterRestaurantDTO(
                "",
                "123",
                "(11)98765-4321",
                "invalid-email",
                "123",
                -1,
                BigDecimal.valueOf(-5)
        );

        doThrow(new ConstraintViolationException("Validation failed", null))
                .when(authService).registerRestaurant(any(RegisterRestaurantDTO.class));

        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> authenticationController.registerRestaurant(invalidDTO)
        );

        assertEquals("Validation failed", exception.getMessage());
        verify(authService, times(1)).registerRestaurant(any(RegisterRestaurantDTO.class));
    }
}