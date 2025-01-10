package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
import com.api.the_chef_backend.model.dtos.request.CategoryRequestDTO;
import com.api.the_chef_backend.model.dtos.response.CategoryResponseDTO;
import com.api.the_chef_backend.model.entity.Category;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.CategoryRepository;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.specification.CategorySpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public CategoryResponseDTO getCategoryById(UUID restaurantId, Long categoryId) {
        log.info("[CategoryService.getCategoryById] Begin - restaurantId: {}, categoryId: {}", restaurantId, categoryId);

        verifyRestaurantIdExists(restaurantId);
        Category category = verifyCategoryIdExists(categoryId);

        if (!category.getRestaurant().getId().equals(restaurantId)) {
            log.error("[CategoryService.getCategoryById] End - Category not found for specified restaurant");
            throw new EntityNotFoundException("Categoria não encontrada para o restaurante especificado.");
        }

        log.info("[CategoryService.getCategoryById] End - Category found: {}", category);
        return new CategoryResponseDTO(category);
    }

    public Page<CategoryResponseDTO> getAllCategories(String name, UUID restaurantId, Pageable pageable) {
        log.info("[CategoryService.getAllCategories] Begin - name: {}, restaurantId: {}", name, restaurantId);

        Specification<Category> specification = CategorySpecification.withFilters(name, restaurantId);
        Page<Category> categoriesPage = categoryRepository.findAll(specification, pageable);

        log.info("[CategoryService.getAllCategories] End - Retrieved {} categories", categoriesPage.getSize());
        return categoriesPage.map(CategoryResponseDTO::new);
    }

    @Transactional
    public CategoryResponseDTO createCategory(UUID restaurantId, CategoryRequestDTO dto) {
        log.info("[CategoryService.createCategory] Begin - restaurantId: {}, request: {}", restaurantId, dto);

        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);

        verifyCategoryNameExistsInRestaurant(restaurantId, dto.name());

        Category category = Category.builder()
                .name(dto.name())
                .restaurant(restaurant)
                .build();

        categoryRepository.save(category);
        log.info("[CategoryService.createCategory] End - Category created: {}", category);

        return new CategoryResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO alterCategory(UUID restaurantId, Long categoryId, CategoryRequestDTO dto) {
        log.info("[CategoryService.alterCategory] Begin - restaurantId: {}, categoryId: {}, request: {}", restaurantId, categoryId, dto);

        Category category = verifyCategoryIdExists(categoryId);
        Restaurant restaurant = verifyRestaurantIdExists(restaurantId);

        verifyCategoryNameExistsInRestaurant(restaurantId, dto.name());

        if (!category.getRestaurant().getId().equals(restaurant.getId())) {
            log.error("[CategoryService.alterCategory] End - Category not found for specified restaurant");
            throw new EntityNotFoundException("Categoria não encontrada para o restaurante especificado.");
        }

        category.alterCategory(dto, restaurant);
        categoryRepository.save(category);

        log.info("[CategoryService.alterCategory] End - Category updated: {}", category);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public void deleteCategory(UUID restaurantId, Long id) {
        log.info("[CategoryService.deleteCategory] Begin - restaurantId: {}, categoryId: {}", restaurantId, id);

        Category category = verifyCategoryIdExists(id);

        if (!category.getRestaurant().getId().equals(restaurantId)) {
            log.error("[CategoryService.deleteCategory] End - Category not found for specified restaurant");
            throw new EntityNotFoundException("Categoria não encontrada para o restaurante especificado.");
        }

        categoryRepository.deleteById(id);
        log.info("[CategoryService.deleteCategory] End - Category deleted with id: {}", id);
    }

    private Category verifyCategoryIdExists(Long id) {
        log.info("[CategoryService.verifyCategoryIdExists] Checking if category exists with id: {}", id);
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.error("[CategoryService.verifyCategoryIdExists] Category not found with id: {}", id);
            return new EntityNotFoundException("Categoria não encontrada com esse id.");
        });
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        log.info("[CategoryService.verifyRestaurantIdExists] Checking if restaurant exists with id: {}", id);
        return restaurantRepository.findById(id).orElseThrow(() -> {
            log.error("[CategoryService.verifyRestaurantIdExists] Restaurant not found with id: {}", id);
            return new EntityNotFoundException("Restaurante não encontrado com esse id.");
        });
    }

    private void verifyCategoryNameExistsInRestaurant(UUID restaurantId, String name) {
        log.info("[CategoryService.verifyCategoryNameExistsInRestaurant] Checking if category name exists in restaurant: {}", name);
        if (categoryRepository.existsByNameAndRestaurantId(name, restaurantId)) {
            log.error("[CategoryService.verifyCategoryNameExistsInRestaurant] Category name already exists in restaurant");
            throw new ConflictException("O nome da categoria já existe.");
        }
    }
}