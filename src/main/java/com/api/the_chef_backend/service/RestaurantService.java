package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exceptions.ConflictException;
import com.api.the_chef_backend.exceptions.InvalidCpfOrCnpjException;
import com.api.the_chef_backend.model.dtos.request.RestaurantRequestDTO;
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

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

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
    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO dto) {
        if (restaurantRepository.existsByCpfCnpj(dto.cpfOrCnpj())) {
            throw new ConflictException("O restaurante já foi registrado.");
        }
        if (CpfCnpjValidatorUtil.isValidCpfOrCnpj(dto.cpfOrCnpj())) {
            throw new InvalidCpfOrCnpjException("O CPF ou CNPJ fornecido é inválido.");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(dto.name())
                .cpfCnpj(dto.cpfOrCnpj())
                .phone(dto.phone())
                .tableQuantity(dto.tableQuantity())
                .waiterCommission(dto.waiterCommission())
                .build();

        restaurantRepository.save(restaurant);
        return new RestaurantResponseDTO(restaurant);
    }

    @Transactional
    public RestaurantResponseDTO alterRestaurant(UUID id, RestaurantRequestDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(id);

        if (CpfCnpjValidatorUtil.isValidCpfOrCnpj(dto.cpfOrCnpj())) {
            throw new InvalidCpfOrCnpjException("O CPF ou CNPJ fornecido é inválido.");
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
