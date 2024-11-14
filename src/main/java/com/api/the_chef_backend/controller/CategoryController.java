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
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO dto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories(@RequestParam(required = false) String name, @RequestParam(required = false) UUID restaurantId, Pageable pageable) {
        return categoryService.getAllCategories(name, restaurantId, pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO newCategory = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> alterCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO existingCategory = categoryService.alterCategory(id, dto);
        return ResponseEntity.ok(existingCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
