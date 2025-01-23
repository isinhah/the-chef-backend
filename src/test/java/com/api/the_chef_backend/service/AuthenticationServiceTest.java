package com.api.the_chef_backend.service;

import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterRestaurantDTO restaurantRequest;
    private Restaurant savedRestaurant;

    @BeforeEach
    void setUp() {
        restaurantRequest = new RegisterRestaurantDTO(
                "Sushi & Sashimi",
                "12345678901234",
                "(11)98765-4321",
                "contato@sushisashimi.com",
                "senha123",
                10,
                new BigDecimal("15.00")
        );

        savedRestaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .name(restaurantRequest.name())
                .email(restaurantRequest.email())
                .cpfCnpj(restaurantRequest.cpfOrCnpj())
                .phone(restaurantRequest.phone())
                .tableQuantity(restaurantRequest.tableQuantity())
                .waiterCommission(restaurantRequest.waiterCommission())
                .password("encodedPassword")
                .build();
    }

    @Test
    void registerRestaurant_WhenSuccessful_ShouldRegisterAndReturnAuthResponse() {
        when(restaurantRepository.existsByEmail(restaurantRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(restaurantRequest.password())).thenReturn("encodedPassword");
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        AuthResponseDTO response = authService.registerRestaurant(restaurantRequest);

        assertNotNull(response);
        assertEquals(restaurantRequest.name(), response.name());
        assertEquals(restaurantRequest.email(), response.email());

        verify(restaurantRepository).existsByEmail(restaurantRequest.email());
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(passwordEncoder).encode(restaurantRequest.password());
    }
}