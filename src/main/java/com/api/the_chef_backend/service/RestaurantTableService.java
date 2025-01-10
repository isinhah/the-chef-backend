package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
import com.api.the_chef_backend.model.dtos.request.RestaurantTableRequestDTO;
import com.api.the_chef_backend.model.dtos.response.RestaurantTableResponseDTO;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.entity.RestaurantTable;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.model.repository.RestaurantTableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantTableResponseDTO getTableById(UUID restaurantId, Long id) {
        log.info("[RestaurantTableService.getTableById] Begin - restaurantId: {}, tableId: {}", restaurantId, id);

        verifyRestaurantIdExists(restaurantId);
        RestaurantTable table = verifyTableIdExists(id);

        log.info("[RestaurantTableService.getTableById] End - table: {}", table);
        return new RestaurantTableResponseDTO(table);
    }

    public Page<RestaurantTableResponseDTO> getAllTables(UUID restaurantId, Pageable pageable) {
        log.info("[RestaurantTableService.getAllTables] Begin - restaurantId: {}", restaurantId);

        Page<RestaurantTable> tablesPage = restaurantTableRepository.findAllByRestaurantId(restaurantId, pageable);

        log.info("[RestaurantTableService.getAllTables] End - tablesPage size: {}", tablesPage.getSize());
        return tablesPage.map(RestaurantTableResponseDTO::new);
    }

    @Transactional
    public RestaurantTableResponseDTO createTable(UUID restaurantId, RestaurantTableRequestDTO dto) {
        log.info("[RestaurantTableService.createTable] Begin - restaurantId: {}, request: {}", restaurantId, dto);

        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);

        verifyTableNumberExistsInRestaurant(restaurantId, dto.tableNumber());

        RestaurantTable table = RestaurantTable.builder()
                .name(dto.name())
                .tableNumber(dto.tableNumber())
                .restaurant(restaurant)
                .build();

        restaurantTableRepository.save(table);

        log.info("[RestaurantTableService.createTable] End - table created: {}", table);
        return new RestaurantTableResponseDTO(table);
    }

    @Transactional
    public RestaurantTableResponseDTO alterTable(UUID restaurantId, Long tableId, RestaurantTableRequestDTO dto) {
        log.info("[RestaurantTableService.alterTable] Begin - restaurantId: {}, tableId: {}, request: {}", restaurantId, tableId, dto);

        RestaurantTable table = verifyTableIdExists(tableId);
        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);

        if (!table.getRestaurant().getId().equals(restaurant.getId())) {
            throw new EntityNotFoundException("Mesa não encontrada para o restaurante especificado.");
        }

        if (table.getTableNumber() != dto.tableNumber()) {
            verifyTableNumberExistsInRestaurant(restaurantId, dto.tableNumber());
        }

        table.alterTable(dto, restaurant);
        restaurantTableRepository.save(table);

        log.info("[RestaurantTableService.alterTable] End - table updated: {}", table);
        return new RestaurantTableResponseDTO(table);
    }

    @Transactional
    public void deleteTable(Long id) {
        log.info("[RestaurantTableService.deleteTable] Begin - tableId: {}", id);

        verifyTableIdExists(id);
        restaurantTableRepository.deleteById(id);

        log.info("[RestaurantTableService.deleteTable] End - table deleted with id: {}", id);
    }

    private RestaurantTable verifyTableIdExists(Long id) {
        log.info("[RestaurantTableService.verifyTableIdExists] Checking if table exists with id: {}", id);
        return restaurantTableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        log.info("[RestaurantTableService.verifyRestaurantIdExists] Checking if restaurant exists with id: {}", id);
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private void verifyTableNumberExistsInRestaurant(UUID restaurantId, int tableNumber) {
        log.info("[RestaurantTableService.verifyTableNumberExistsInRestaurant] Checking if table number {} exists in restaurant with id: {}", tableNumber, restaurantId);
        if (restaurantTableRepository.existsByRestaurantIdAndTableNumber(restaurantId, tableNumber)) {
            throw new ConflictException("O número da mesa já existe.");
        }
    }
}