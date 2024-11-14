package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exceptions.ConflictException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = verifyCategoryIdExists(id);
        return new CategoryResponseDTO(category);
    }

    public Page<CategoryResponseDTO> getAllCategories(String name, UUID restaurantId, Pageable pageable) {
        Specification<Category> specification = CategorySpecification.withFilters(name, restaurantId);
        Page<Category> categoriesPage = categoryRepository.findAll(specification, pageable);
        return categoriesPage.map(CategoryResponseDTO::new);
    }

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());
        verifyCategoryNameExistsInRestaurant(dto);

        Category category = Category.builder()
                .name(dto.name())
                .restaurant(restaurant)
                .build();

        categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO alterCategory(Long id, CategoryRequestDTO dto) {
        Category category = verifyCategoryIdExists(id);
        Restaurant restaurant = verifyRestaurantIdExists(dto.restaurantId());
        verifyCategoryNameExistsInRestaurant(dto);

        category.alterCategory(dto, restaurant);

        categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        verifyCategoryIdExists(id);
        categoryRepository.deleteById(id);
    }

    private Category verifyCategoryIdExists(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com esse id."));
    }

    private Restaurant verifyRestaurantIdExists(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com esse id."));
    }

    private void verifyCategoryNameExistsInRestaurant(CategoryRequestDTO dto) {
        if (categoryRepository.existsByNameAndRestaurantId(dto.name(), dto.restaurantId())) {
            throw new ConflictException("O nome da categoria já existe.");
        }
    }
}