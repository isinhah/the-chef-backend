package com.api.the_chef_backend.service;

import com.api.the_chef_backend.model.dtos.request.CategoryRequestDTO;
import com.api.the_chef_backend.model.dtos.response.CategoryResponseDTO;
import com.api.the_chef_backend.model.entity.Category;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.CategoryRepository;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CategoryService categoryService;

    private UUID restaurantId;
    private Long categoryId;
    private CategoryRequestDTO categoryRequestDTO;
    private Category category;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        categoryId = 1L;
        categoryRequestDTO = new CategoryRequestDTO("Sushi");

        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setCategories(new ArrayList<>());

        category = new Category();
        category.setId(categoryId);
        category.setName("Sushi");
        category.setRestaurant(restaurant);

        restaurant.getCategories().add(category);
    }

    @Test
    void getCategoryById_ValidCategory_ReturnCategoryResponseDTO() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryResponseDTO responseDTO = categoryService.getCategoryById(restaurantId, categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        assert responseDTO != null;
    }

    @Test
    void createCategory_SuccessfulCreation_ReturnCategoryResponseDTO() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.existsByNameAndRestaurantId(categoryRequestDTO.name(), restaurantId))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO responseDTO = categoryService.createCategory(restaurantId, categoryRequestDTO);

        assert responseDTO != null;
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void alterCategory_SuccessfulUpdate_ReturnCategoryResponseDTO() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO responseDTO = categoryService.alterCategory(restaurantId, categoryId, categoryRequestDTO);

        assert responseDTO != null;
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategory_SuccessfulDeletion_NoException() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        doNothing().when(categoryRepository).deleteById(categoryId);

        assertDoesNotThrow(() -> categoryService.deleteCategory(restaurantId, categoryId));

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
