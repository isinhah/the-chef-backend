package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
import com.api.the_chef_backend.model.dtos.request.ProductExtraRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductExtraResponseDTO;
import com.api.the_chef_backend.model.entity.ProductExtra;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.ProductExtraRepository;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
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
public class ProductExtraService {

    private final ProductExtraRepository productExtraRepository;
    private final RestaurantRepository restaurantRepository;

    public ProductExtraResponseDTO getComplementById(UUID restaurantId, Long complementId) {
        log.info("[ProductExtraService.getComplementById] Begin - restaurantId: {}, complementId: {}", restaurantId, complementId);

        verifyRestaurantIdExists(restaurantId);
        ProductExtra complement = verifyComplementIdExists(complementId);

        log.info("[ProductExtraService.getComplementById] End - complement: {}", complement);
        return new ProductExtraResponseDTO(complement);
    }

    public Page<ProductExtraResponseDTO> getAllComplements(UUID restaurantId, Pageable pageable) {
        log.info("[ProductExtraService.getAllComplements] Begin - restaurantId: {}", restaurantId);

        verifyRestaurantIdExists(restaurantId);
        Page<ProductExtra> complements = productExtraRepository.findAllByRestaurantId(restaurantId, pageable);

        log.info("[ProductExtraService.getAllComplements] End - complements size: {}", complements.getSize());
        return complements.map(ProductExtraResponseDTO::new);
    }

    @Transactional
    public ProductExtraResponseDTO createComplement(UUID restaurantId, ProductExtraRequestDTO dto) {
        log.info("[ProductExtraService.createComplement] Begin - restaurantId: {}, request: {}", restaurantId, dto);

        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);

        ProductExtra productExtra = ProductExtra.builder()
                .name(dto.name())
                .price(dto.price())
                .stock(dto.stock())
                .restaurant(restaurant)
                .build();

        productExtraRepository.save(productExtra);

        log.info("[ProductExtraService.createComplement] End - productExtra created: {}", productExtra);
        return new ProductExtraResponseDTO(productExtra);
    }

    @Transactional
    public ProductExtraResponseDTO alterComplement(UUID restaurantId, Long complementId, ProductExtraRequestDTO dto) {
        log.info("[ProductExtraService.alterComplement] Begin - restaurantId: {}, complementId: {}, request: {}", restaurantId, complementId, dto);

        ProductExtra complement = verifyComplementIdExists(complementId);
        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);

        verifyComplementNameExistsInRestaurant(restaurantId, dto.name());

        if (!complement.getRestaurant().getId().equals(restaurant.getId())) {
            throw new EntityNotFoundException("Complement not found for the specified restaurant.");
        }

        complement.alterComplement(dto, restaurant);
        productExtraRepository.save(complement);

        log.info("[ProductExtraService.alterComplement] End - complement updated: {}", complement);
        return new ProductExtraResponseDTO(complement);
    }

    @Transactional
    public void deleteComplement(UUID restaurantId, Long id) {
        log.info("[ProductExtraService.deleteComplement] Begin - restaurantId: {}, complementId: {}", restaurantId, id);

        verifyRestaurantIdExists(restaurantId);
        ProductExtra complement = verifyComplementIdExists(id);

        if (!complement.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Complement not found for the specified restaurant.");
        }

        productExtraRepository.deleteById(id);

        log.info("[ProductExtraService.deleteComplement] End - complement deleted with id: {}", id);
    }

    private ProductExtra verifyComplementIdExists(Long id) {
        log.info("[ProductExtraService.verifyComplementIdExists] Checking if complement exists with id: {}", id);
        return productExtraRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Complement not found with this id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        log.info("[ProductExtraService.verifyRestaurantIdExists] Checking if restaurant exists with id: {}", id);
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with this id."));
    }

    private void verifyComplementNameExistsInRestaurant(UUID restaurantId, String name) {
        log.info("[ProductExtraService.verifyComplementNameExistsInRestaurant] Checking if complement with name '{}' exists in restaurant with id: {}", name, restaurantId);
        if (productExtraRepository.existsByNameAndRestaurantId(name, restaurantId)) {
            throw new ConflictException("The complement name already exists.");
        }
    }
}