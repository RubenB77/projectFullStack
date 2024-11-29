package com.app.springBack.controller;

import com.app.springBack.dto.CategoryDto;
import com.app.springBack.model.Category;
import com.app.springBack.repository.CategoryRepository;
import com.app.springBack.service.CategoryService;

import jakarta.validation.Valid;

import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class CategoryController {
    
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Iterable<Category> getCategories() {
        return this.categoryRepository.findAll();
    }

    @PostMapping("/categories")
    public Iterable<Category> initCategories(@Valid @RequestBody Iterable<Category> categories) {
        for (Category category : categories) {
            this.categoryRepository.save(category);
        }
        return this.categoryRepository.findAll();
    }

    @PostMapping("/category")
    public Category postCategory(@Valid @RequestBody Category category) {
        return this.categoryRepository.save(category);
    }

    @PutMapping("/category/{id}") 
    public Category putCategory(@PathVariable("id") int id, @Valid @RequestBody CategoryDto categoryDto) {
        return this.categoryService.updateCategory(id, categoryDto);
    }

    @GetMapping("/user/{id}/categories") 
    public Iterable<CategoryDto> getUserCategories(@Valid @PathVariable("id") int userId){
        return this.categoryService.getCategoriesByUserId(userId)
                .stream()
                .map(expense -> new CategoryDto(expense))
                .collect(Collectors.toList());
    }

    @PostMapping("/user/{id}/category")
    public Category postCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("id") int userId) {
        Category category = this.categoryService.createCategoryFromDto(categoryDto, userId);
        return this.categoryRepository.save(category);
    }

}
