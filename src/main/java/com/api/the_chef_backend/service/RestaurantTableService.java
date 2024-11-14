package com.api.the_chef_backend.service;

import com.api.the_chef_backend.model.dtos.request.RestaurantTableRequestDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantTableResponseDTO;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.entity.RestaurantTable;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.model.repository.RestaurantTableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantTableResponseDTO getTableById(UUID restaurantId, Long id) {
        verifyRestaurantIdExists(restaurantId);
        RestaurantTable table = verifyTableIdExists(id);
        return new RestaurantTableResponseDTO(table);
    }

    public Page<RestaurantTableResponseDTO> getAllTables(UUID restaurantId, Pageable pageable) {
        Page<RestaurantTable> tablesPage = restaurantTableRepository.findAllByRestaurantId(restaurantId, pageable);
        return tablesPage.map(RestaurantTableResponseDTO::new);
    }

    @Transactional
    public RestaurantTableResponseDTO createTable(RestaurantTableRequestDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());

        RestaurantTable table = RestaurantTable.builder()
                .name(dto.name())
                .tableNumber(dto.tableNumber())
                .restaurant(restaurant)
                .build();

        restaurantTableRepository.save(table);
        return new RestaurantTableResponseDTO(table);
    }

    @Transactional
    public RestaurantTableResponseDTO alterTable(Long id, RestaurantTableRequestDTO dto) {
        RestaurantTable table = verifyTableIdExists(id);
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());

        table.alterTable(dto, restaurant);

        restaurantTableRepository.save(table);
        return new RestaurantTableResponseDTO(table);
    }

    @Transactional
    public void deleteTable(Long id) {
        verifyTableIdExists(id);
        restaurantTableRepository.deleteById(id);
    }

    private RestaurantTable verifyTableIdExists(Long id) {
        return restaurantTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }
}