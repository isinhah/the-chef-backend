package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exceptions.ConflictException;
import com.api.the_chef_backend.model.dtos.request.ProductExtraRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductExtraResponseDTO;
import com.api.the_chef_backend.model.entity.ProductExtra;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.ProductExtraRepository;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductExtraService {

    private final ProductExtraRepository productExtraRepository;
    private final RestaurantRepository restaurantRepository;

    public ProductExtraResponseDTO getComplementById(UUID restaurantId, Long complementId) {
        verifyRestaurantIdExists(restaurantId);
        ProductExtra complement = verifyComplementIdExists(complementId);

        return new ProductExtraResponseDTO(complement);
    }

    public Page<ProductExtraResponseDTO> getAllComplements(UUID restaurantId, Pageable pageable) {
        verifyRestaurantIdExists(restaurantId);

        Page<ProductExtra> complements = productExtraRepository.findAllByRestaurantId(restaurantId, pageable);

        return complements.map(ProductExtraResponseDTO::new);
    }

    @Transactional
    public ProductExtraResponseDTO createComplement(ProductExtraRequestDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());

        ProductExtra productExtra = ProductExtra.builder()
                .name(dto.name())
                .price(dto.price())
                .stock(dto.stock())
                .restaurant(restaurant)
                .build();

        productExtraRepository.save(productExtra);

        return new ProductExtraResponseDTO(productExtra);
    }

    @Transactional
    public ProductExtraResponseDTO alterComplement(Long id, ProductExtraRequestDTO dto) {
        ProductExtra complement = verifyComplementIdExists(id);
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());
        verifyComplementNameExistsInRestaurant(dto);

        if (!complement.getRestaurant().getId().equals(restaurant.getId())) {
            throw new EntityNotFoundException("Complemento não encontrado para o restaurante especificado.");
        }

        complement.alterComplement(dto, restaurant);

        productExtraRepository.save(complement);
        return new ProductExtraResponseDTO(complement);
    }

    @Transactional
    public void deleteComplement(UUID restaurantId, Long id) {
        verifyRestaurantIdExists(restaurantId);
        ProductExtra complement = verifyComplementIdExists(id);

        if (!complement.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Complemento não encontrado para o restaurante especificado.");
        }

        productExtraRepository.deleteById(id);
    }

    private ProductExtra verifyComplementIdExists(Long id) {
        return productExtraRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Complemento não encontrado com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private void verifyComplementNameExistsInRestaurant(ProductExtraRequestDTO dto) {
        if (productExtraRepository.existsByNameAndRestaurantId(dto.name(), dto.restaurantId())) {
            throw new ConflictException("O nome do complemento já existe.");
        }
    }
}