package com.app.springBack.dto;

import com.app.springBack.model.Category;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;

public class CategoryDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;

    private String categoryDescription;

    private String categoryName;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid HEX code" )
    private String categoryColor = "#e0e0e0"; 

    private int categoryUserId;

    public CategoryDto(int categoryId, String categoryName, String categoryColor, String categoryDescription, int categoryUserId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.categoryDescription = categoryDescription;
        this.categoryUserId = categoryUserId;
    }

    public CategoryDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categoryColor = category.getCategoryColor();
        this.categoryDescription = category.getCategoryDescription();
        if (category.getCategoryUser() != null) {
            this.categoryUserId = category.getCategoryUser().getId();
        }
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getCategoryDescription() {
        return this.categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public int getCategoryUserId() {
        return this.categoryUserId;
    }

    public void setCategoryUserId(int categoryUserId) {
        this.categoryUserId = categoryUserId;
    }
}
