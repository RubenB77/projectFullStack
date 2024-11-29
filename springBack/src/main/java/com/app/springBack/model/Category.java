package com.app.springBack.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.app.springBack.dto.CategoryDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;

    @NotNull
    private String categoryName;
    
    @NotNull
    private String categoryDescription;

    @NotNull
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid HEX code" )
    private String categoryColor = "#e0e0e0";

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "expenseCategory", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User categoryUser;

    protected Category() {}

    public Category(String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public Category(String categoryName, String categoryDescription, String categoryColor) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryColor = categoryColor;
    }

    public Category(String categoryName, String categoryDescription, String categoryColor, User categoryUser) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryColor = categoryColor;
        this.categoryUser = categoryUser;
    }
    public Category(CategoryDto categoryDto, User user) {
        this.categoryName = categoryDto.getCategoryName();
        this.categoryDescription = categoryDto.getCategoryDescription();
        if(categoryDto.getCategoryColor() != null) {
            this.categoryColor = categoryDto.getCategoryColor();
        }
        this.categoryUser = user;
    }

    @PrePersist
    public void onInsert() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Europe/Paris")).truncatedTo(ChronoUnit.SECONDS);
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Europe/Paris")).truncatedTo(ChronoUnit.SECONDS);
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }


    public String getCategoryName() {
        return this.categoryName;
    }

    public String getCategoryDescription() {
        return this.categoryDescription;
    }

    public String getCategoryColor() {
        return this.categoryColor;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public User getCategoryUser() {
        return this.categoryUser;
    }

    public void setCategoryUser(User categoryUser) {
        this.categoryUser = categoryUser;
    }

}
