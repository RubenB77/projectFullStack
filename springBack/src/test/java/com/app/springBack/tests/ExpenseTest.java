package com.app.springBack.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.springBack.model.Category;
import com.app.springBack.model.Expense;
import com.app.springBack.model.User;
import com.app.springBack.repository.CategoryRepository;
import com.app.springBack.repository.ExpenseRepository;
import com.app.springBack.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private User user;
    private Category category;

    @BeforeEach
    public void beforeEach() {
         user = new User("johnD", "Password123", "USER", "john.doe@example.com");
         userRepository.save(user);
 
         category = new Category("Groceries", "Food-related expenses", "#FF5733", user);
         categoryRepository.save(category);
    }

    @AfterEach
    public void afterEach() {
        categoryRepository.deleteAll(); 
        userRepository.deleteAll();    
        expenseRepository.deleteAll();
    }


    @Test
    public void testCreateExpense() {
        // Create an Expense
        Expense expense = new Expense("Grocery shopping", new BigDecimal("50.00"), "Bought vegetables and fruits", LocalDate.now(), category, user);

        // Save Expense
        try {
            expenseRepository.save(expense);
            assertNotNull(expense.getId(), "Expense ID should not be null");
        } catch (Exception e) {
            fail("Saving expense failed: " + e.getMessage());
        }

        // Assert the expense is saved correctly
        assertEquals("Grocery shopping", expense.getExpenseName(), "Expense name should match");
        assertEquals(new BigDecimal("50.00"), expense.getExpensePrice(), "Expense price should match");
        assertEquals(user.getId(), expense.getUser().getId(), "Expense should be linked to the correct user");
        assertEquals(category.getCategoryId(), expense.getExpenseCategory().getCategoryId(), "Expense should be linked to the correct category");

        // Fetch the expense from the database and validate
        Optional<Expense> fetchedExpense = expenseRepository.findById(expense.getId());
        assertTrue(fetchedExpense.isPresent(), "Expense should be present in the database");
        assertEquals("Grocery shopping", fetchedExpense.get().getExpenseName());
    }
}
