package com.app.springBack.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.springBack.dto.CategoryDto;
import com.app.springBack.model.Category;
import com.app.springBack.model.User;
import com.app.springBack.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
    
    private CategoryRepository categoryRepository;
    private UserService userService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    public Category findCategoryById(int categoryId) {
        return this.categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", categoryId)));
    }

    @Override
    public Category updateCategory(int categoryId, CategoryDto updatedCategory) {
        Category category = this.findCategoryById(categoryId);

        if (updatedCategory.getCategoryName() != null) {
            category.setCategoryName(updatedCategory.getCategoryName());
        }
        if (updatedCategory.getCategoryDescription() != null) {
            category.setCategoryDescription(updatedCategory.getCategoryDescription());
        }
        if (updatedCategory.getCategoryColor() != null) {
            category.setCategoryColor(updatedCategory.getCategoryColor());
        }

        this.categoryRepository.save(category);

        return category;
    }

    @Override
    public Category createCategoryFromDto(CategoryDto categoryDto, int userId) {

        User categoryUser = this.userService.getByUserId(userId);

        return new Category(categoryDto, categoryUser);

    }

    @Override
    public List<Category> getCategoriesByUserId(int userId) {
        return this.categoryRepository.findByCategoryUserId(userId);
    }
}
