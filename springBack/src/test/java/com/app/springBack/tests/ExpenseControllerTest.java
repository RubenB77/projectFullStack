package com.app.springBack.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.app.springBack.auth.LoginDto;
import com.app.springBack.dto.ExpenseDtoInput;
import com.app.springBack.model.Category;
import com.app.springBack.model.User;
import com.app.springBack.repository.CategoryRepository;
import com.app.springBack.repository.ExpenseRepository;
import com.app.springBack.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;  
    @Autowired
    private CategoryRepository categoryRepository;  
    @Autowired
    private ExpenseRepository expenseRepository;

    private User testUser;
    private Category testCategory;

    // Encode the password
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodedPassword = encoder.encode("Password123");

    @BeforeEach
    public void beforeEach() throws Exception {


        testUser = new User("johnD", encodedPassword, "USER", "john.doe@example.com");
        userRepository.save(testUser);
 
        testCategory = new Category("Groceries", "Food-related expenses", "#FF5733", testUser);
        categoryRepository.save(testCategory);

        LoginDto loginDto = new LoginDto("johnD", "Password123");  // Assuming this is your Login DTO
        String json = new ObjectMapper().writeValueAsString(loginDto);

        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @AfterEach
    public void afterEach() {
        categoryRepository.deleteAll(); 
        userRepository.deleteAll();    
        expenseRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "johnD", password = "Password123", roles = {"USER"})
    public void testCreateExpenseForUser() throws Exception {
        // Create ExpenseDtoInput object
        ExpenseDtoInput expenseDtoInput = new ExpenseDtoInput(
            "Test Expense",
            new BigDecimal("100.50"),
            "Testing expense creation",
            LocalDate.now(),
            null, // Assuming address is optional
            1,    // Category ID
            ZonedDateTime.now()
        );

        // Convert the DTO to JSON
        String expenseRequestJson = objectMapper.writeValueAsString(expenseDtoInput);

        // Perform POST request
        mockMvc.perform(
                post("/user/1/expense")
                        .contentType("application/json")
                        .content(expenseRequestJson)
                        .header("Authorization", "Bearer test-token")) // Mock token
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.expenseName").value("Test Expense"))
                .andExpect(jsonPath("$.expensePrice").value(100.50))
                .andExpect(jsonPath("$.expenseNote").value("Testing expense creation"));
    }
}
