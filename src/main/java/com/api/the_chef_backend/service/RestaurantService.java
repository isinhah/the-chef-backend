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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Optional<Restaurant> getRestaurantByEmail(String email) {
        log.info("[RestaurantService.getRestaurantByEmail] Begin - email: {}", email);
        Optional<Restaurant> restaurant = restaurantRepository.findByEmail(email);
        log.info("[RestaurantService.getRestaurantByEmail] End - result: {}", restaurant.isPresent() ? "Found" : "Not Found");
        return restaurant;
    }

    public RestaurantResponseDTO getRestaurantById(UUID id) {
        log.info("[RestaurantService.getRestaurantById] Begin - id: {}", id);
        Restaurant restaurant = verifyRestaurantIdExists(id);
        RestaurantResponseDTO response = new RestaurantResponseDTO(restaurant);
        log.info("[RestaurantService.getRestaurantById] End - response: {}", response);
        return response;
    }

    public Page<RestaurantResponseDTO> getAllRestaurants(String name, String phone, Pageable pageable) {
        log.info("[RestaurantService.getAllRestaurants] Begin - name: {}, phone: {}", name, phone);
        Specification<Restaurant> specification = RestaurantSpecification.withFilters(name, phone);
        Page<Restaurant> restaurantsPage = restaurantRepository.findAll(specification, pageable);
        log.info("[RestaurantService.getAllRestaurants] End - total restaurants: {}", restaurantsPage.getTotalElements());
        return restaurantsPage.map(RestaurantResponseDTO::new);
    }

    @Transactional
    public RestaurantResponseDTO alterRestaurant(UUID id, RegisterRestaurantDTO dto) {
        log.info("[RestaurantService.alterRestaurant] Begin - id: {}, dto: {}", id, dto);

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

        RestaurantResponseDTO response = new RestaurantResponseDTO(restaurant);
        log.info("[RestaurantService.alterRestaurant] End - response: {}", response);
        return response;
    }

    @Transactional
    public void deleteRestaurant(UUID id) {
        log.info("[RestaurantService.deleteRestaurant] Begin - id: {}", id);
        verifyRestaurantIdExists(id);
        restaurantRepository.deleteById(id);
        log.info("[RestaurantService.deleteRestaurant] End - id: {} deleted", id);
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[RestaurantService.verifyRestaurantIdExists] Restaurant not found for id: {}", id);
                    return new EntityNotFoundException("Restaurante não encontrado com esse id.");
                });
    }
}
