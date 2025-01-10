package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public ProductResponseDTO getProductById(UUID restaurantId, Long productId) {
        log.info("[ProductService.getProductById] Begin - restaurantId: {}, productId: {}", restaurantId, productId);

        verifyRestaurantIdExists(restaurantId);
        Product product = verifyProductIdExists(productId);

        log.info("[ProductService.getProductById] End - product: {}", product);
        return new ProductResponseDTO(product);
    }

    public Page<ProductResponseDTO> getAllProducts(String name, UUID restaurantId, Pageable pageable) {
        log.info("[ProductService.getAllProducts] Begin - name: {}, restaurantId: {}", name, restaurantId);

        Page<Product> products;
        if (name != null && !name.isBlank()) {
            products = productRepository.findByNameAndRestaurantId(name, restaurantId, pageable);
        } else {
            products = productRepository.findByRestaurantId(restaurantId, pageable);
        }

        log.info("[ProductService.getAllProducts] End - products size: {}", products.getSize());
        return products.map(ProductResponseDTO::new);
    }

    @Transactional
    public ProductResponseDTO createProduct(UUID restaurantId, ProductRequestDTO dto) {
        log.info("[ProductService.createProduct] Begin - restaurantId: {}, request: {}", restaurantId, dto);

        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);
        Category category = verifyCategoryIdExists(dto.categoryId());

        verifyProductNameExistsInRestaurant(restaurantId, dto.name());

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

        log.info("[ProductService.createProduct] End - product created: {}", product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO alterProduct(UUID restaurantId, Long productId, ProductRequestDTO dto) {
        log.info("[ProductService.alterProduct] Begin - restaurantId: {}, productId: {}, request: {}", restaurantId, productId, dto);

        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);
        Product product = verifyProductIdExists(productId);
        Category category = verifyCategoryIdExists(dto.categoryId());

        if (!product.getRestaurant().getId().equals(restaurant.getId())) {
            throw new EntityNotFoundException("Produto não encontrado no restaurante especificado.");
        }

        verifyProductNameExistsInRestaurant(restaurantId, dto.name());

        product.alterProduct(dto, restaurant, category);

        productRepository.save(product);

        log.info("[ProductService.alterProduct] End - product updated: {}", product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public void deleteProduct(UUID restaurantId, Long id) {
        log.info("[ProductService.deleteProduct] Begin - restaurantId: {}, productId: {}", restaurantId, id);

        verifyRestaurantIdExists(restaurantId);
        Product product = verifyProductIdExists(id);

        if (!product.getRestaurant().getId().equals(restaurantId)) {
            throw new EntityNotFoundException("Produto não encontrado no restaurante especificado.");
        }

        productRepository.deleteById(id);

        log.info("[ProductService.deleteProduct] End - product deleted with id: {}", id);
    }

    private Product verifyProductIdExists(Long id) {
        log.info("[ProductService.verifyProductIdExists] Checking if product exists with id: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        log.info("[ProductService.verifyRestaurantIdExists] Checking if restaurant exists with id: {}", id);
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private Category verifyCategoryIdExists(Long id) {
        log.info("[ProductService.verifyCategoryIdExists] Checking if category exists with id: {}", id);
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com esse id."));
    }

    private void verifyProductNameExistsInRestaurant(UUID restaurantId, String name) {
        log.info("[ProductService.verifyProductNameExistsInRestaurant] Checking if product name '{}' exists in restaurant with id: {}", name, restaurantId);
        if (productRepository.existsByNameAndRestaurantId(name, restaurantId)) {
            throw new ConflictException("O nome do produto já existe.");
        }
    }
}