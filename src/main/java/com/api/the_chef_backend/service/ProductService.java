package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exceptions.ConflictException;
import com.api.the_chef_backend.model.dtos.request.ProductRequestDTO;
import com.api.the_chef_backend.model.dtos.response.ProductResponseDTO;
import com.api.the_chef_backend.model.entity.Category;
import com.api.the_chef_backend.model.entity.Product;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.CategoryRepository;
import com.api.the_chef_backend.model.repository.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public ProductResponseDTO getProductById(UUID restaurantId, Long productId) {
        verifyRestaurantIdExists(restaurantId);
        Product product = verifyProductIdExists(productId);

        return new ProductResponseDTO(product);
    }

    public Page<ProductResponseDTO> getAllProducts(String name, UUID restaurantId, Pageable pageable) {
        Page<Product> products;

        if (name != null && !name.isBlank()) {
            products = productRepository.findByNameAndRestaurantId(name, restaurantId, pageable);
        } else {
            products = productRepository.findByRestaurantId(restaurantId, pageable);
        }

        return products.map(ProductResponseDTO::new);
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());
        Category category = verifyCategoryIdExists(dto.categoryId());

        verifyProductNameExistsInRestaurant(dto);

        Product product = Product.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .description(dto.description())
                .price(dto.price())
                .pdvCode(dto.pdvCode())
                .stock(dto.stock())
                .category(category)
                .restaurant(restaurant)
                .build();

        productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO alterProduct(Long id, ProductRequestDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());
        Product product = verifyProductIdExists(id);
        Category category = verifyCategoryIdExists(dto.categoryId());

        if (!product.getRestaurant().getId().equals(restaurant.getId())) {
            throw new EntityNotFoundException("Produto não encontrado  no restaurante especificado.");
        }

        verifyProductNameExistsInRestaurant(dto);

        product.alterProduct(dto, restaurant, category);

        productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public void deleteProduct(UUID restaurantId, Long id) {
        verifyRestaurantIdExists(restaurantId);
        Product product = verifyProductIdExists(id);

        if (!product.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Produto não encontrado no restaurante especificado.");
        }

        productRepository.deleteById(id);
    }

    private Product verifyProductIdExists(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private Category verifyCategoryIdExists(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com esse id."));
    }

    private void verifyProductNameExistsInRestaurant(ProductRequestDTO dto) {
        if (productRepository.existsByNameAndRestaurantId(dto.name(), dto.restaurantId())) {
            throw new ConflictException("O nome do produto já existe.");
        }
    }
}
