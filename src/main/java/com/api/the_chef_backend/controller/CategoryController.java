package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.model.dtos.request.CategoryRequestDTO;
import com.api.the_chef_backend.model.dtos.response.CategoryResponseDTO;
import com.api.the_chef_backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable UUID restaurantId, @PathVariable Long categoryId) {
        CategoryResponseDTO dto = categoryService.getCategoryById(restaurantId, categoryId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories(@PathVariable UUID restaurantId, @RequestParam(required = false) String name, Pageable pageable) {
        return categoryService.getAllCategories(name, restaurantId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO newCategory = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> alterCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO existingCategory = categoryService.alterCategory(categoryId, dto);
        return ResponseEntity.ok(existingCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
