package com.app.springBack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.springBack.dto.CategoryDto;
import com.app.springBack.model.Category;

@Service
public interface CategoryService {
    public Category findCategoryById(int categoryId);
    public Category updateCategory(int categoryId, CategoryDto category);
    public Category createCategoryFromDto(CategoryDto categoryDto, int userId);
    public List<Category> getCategoriesByUserId(int userId);
}
