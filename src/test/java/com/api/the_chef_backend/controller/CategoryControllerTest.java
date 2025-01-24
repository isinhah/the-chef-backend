package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.dtos.request.CategoryRequestDTO;
import com.api.the_chef_backend.model.dtos.response.CategoryResponseDTO;
import com.api.the_chef_backend.service.CategoryService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final UUID restaurantId = UUID.randomUUID();
    private final Long categoryId = 1L;

    public CategoryRequestDTO categoryRequestDTO() {
        return new CategoryRequestDTO("Sushi");
    }

    public CategoryResponseDTO categoryResponseDTO() {
        return new CategoryResponseDTO(categoryId, "Sushi", restaurantId);
    }

    @Test
    void testGetCategoryById_WhenSuccessful() {
        CategoryResponseDTO categoryResponseDTO = categoryResponseDTO();

        when(categoryService.getCategoryById(restaurantId, categoryId)).thenReturn(categoryResponseDTO);

        assertDoesNotThrow(() -> categoryController.getCategoryById(restaurantId, categoryId));

        verify(categoryService, times(1)).getCategoryById(restaurantId, categoryId);
    }

    @Test
    void testGetAllCategories_WhenSuccessful() {
        List<CategoryResponseDTO> categories = List.of(categoryResponseDTO());
        Page<CategoryResponseDTO> pageResponse = new PageImpl<>(categories);

        when(categoryService.getAllCategories(isNull(), eq(restaurantId), any(PageRequest.class)))
                .thenReturn(pageResponse);

        assertDoesNotThrow(() -> categoryController.getAllCategories(restaurantId, null, PageRequest.of(0, 10)));

        verify(categoryService, times(1)).getAllCategories(isNull(), eq(restaurantId), any(PageRequest.class));
    }

    @Test
    void testCreateCategory_WhenSuccessful() {
        CategoryRequestDTO categoryRequestDTO = categoryRequestDTO();
        CategoryResponseDTO categoryResponseDTO = categoryResponseDTO();

        when(categoryService.createCategory(restaurantId, categoryRequestDTO)).thenReturn(categoryResponseDTO);

        assertDoesNotThrow(() -> categoryController.createCategory(restaurantId, categoryRequestDTO));

        verify(categoryService, times(1)).createCategory(restaurantId, categoryRequestDTO);
    }

    @Test
    void testAlterCategory_WhenSuccessful() {
        CategoryRequestDTO categoryRequestDTO = categoryRequestDTO();
        CategoryResponseDTO categoryResponseDTO = categoryResponseDTO();

        when(categoryService.alterCategory(restaurantId, categoryId, categoryRequestDTO)).thenReturn(categoryResponseDTO);

        assertDoesNotThrow(() -> categoryController.alterCategory(restaurantId, categoryId, categoryRequestDTO));

        verify(categoryService, times(1)).alterCategory(restaurantId, categoryId, categoryRequestDTO);
    }

    @Test
    void testDeleteCategory_WhenSuccessful() {
        doNothing().when(categoryService).deleteCategory(restaurantId, categoryId);

        assertDoesNotThrow(() -> categoryController.deleteCategory(restaurantId, categoryId));

        verify(categoryService, times(1)).deleteCategory(restaurantId, categoryId);
    }

    @Test
    void testCreateCategory_WhenValidationFails() {
        CategoryRequestDTO invalidDTO = new CategoryRequestDTO(""); // Nome vazio

        doThrow(new ConstraintViolationException("Validation failed", null))
                .when(categoryService).createCategory(restaurantId, invalidDTO);

        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> categoryController.createCategory(restaurantId, invalidDTO)
        );

        assertEquals("Validation failed", exception.getMessage());
        verify(categoryService, times(1)).createCategory(restaurantId, invalidDTO);
    }
}