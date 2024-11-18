package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
import com.api.the_chef_backend.exception.InvalidCpfOrCnpjException;
import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.util.CpfCnpjValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO registerRestaurant(RegisterRestaurantDTO dto) {
        if (restaurantRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Email já registrado.");
        }
        if (!CpfCnpjValidatorUtil.isValidCpfOrCnpj(dto.cpfOrCnpj())) {
            throw new InvalidCpfOrCnpjException("CPF ou CNPJ inválido.");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(dto.name())
                .email(dto.email())
                .cpfCnpj(dto.cpfOrCnpj())
                .phone(dto.phone())
                .tableQuantity(dto.tableQuantity())
                .waiterCommission(dto.waiterCommission())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        restaurantRepository.save(restaurant);
        return new AuthResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getEmail()
        );
    }
}
