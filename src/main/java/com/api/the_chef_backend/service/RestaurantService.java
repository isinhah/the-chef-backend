package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
import com.api.the_chef_backend.exception.InvalidCpfOrCnpjException;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantResponseDTO;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.specification.RestaurantSpecification;
import com.api.the_chef_backend.util.CpfCnpjValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Optional<Restaurant> getRestaurantByEmail(String email) {
        return restaurantRepository.findByEmail(email);
    }

    public RestaurantResponseDTO getRestaurantById(UUID id) {
        Restaurant restaurant = verifyRestaurantIdExists(id);
        return new RestaurantResponseDTO(restaurant);
    }

    public Page<RestaurantResponseDTO> getAllRestaurants(String name, String phone, Pageable pageable) {
        Specification<Restaurant> specification = RestaurantSpecification.withFilters(name, phone);
        Page<Restaurant> restaurantsPage = restaurantRepository.findAll(specification, pageable);
        return restaurantsPage.map(RestaurantResponseDTO::new);
    }

    @Transactional
    public RestaurantResponseDTO alterRestaurant(UUID id, RegisterRestaurantDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(id);

        if (!restaurant.getEmail().equals(dto.email()) && restaurantRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Email já registrado.");
        }

        if (!restaurant.getCpfCnpj().equals(dto.cpfOrCnpj()) &&
                !CpfCnpjValidatorUtil.isValidCpfOrCnpj(dto.cpfOrCnpj())) {
            throw new InvalidCpfOrCnpjException("CPF ou CNPJ inválido.");
        }

        restaurant.alterRestaurant(dto);

        restaurantRepository.save(restaurant);
        return new RestaurantResponseDTO(restaurant);
    }

    @Transactional
    public void deleteRestaurant(UUID id) {
        verifyRestaurantIdExists(id);
        restaurantRepository.deleteById(id);
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }
}
