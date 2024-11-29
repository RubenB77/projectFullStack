package com.app.springBack.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.springBack.model.Expense;
import com.app.springBack.repository.ExpenseRepository;
import com.app.springBack.service.ExpenseService;
import com.app.springBack.dto.ExpenseDto;
import com.app.springBack.dto.ExpenseDtoInput;

import jakarta.validation.Valid;

@RestController
@Validated
public class ExpenseController {
    
    private final ExpenseRepository expenseRepository;
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseRepository expenseRepository, ExpenseService expenseService) {
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
    }

    @GetMapping("/expenses") 
    public List<Expense> getExpenses() {
        return this.expenseRepository.findAll();
    }

    @GetMapping("/expense/{id}")
    public Expense getExpenseById(@PathVariable("id") int id) {
        return this.expenseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Expense with %id not found", id)));
    }

    @GetMapping("/expenseDto/{id}")
    public ExpenseDto getExpenseDtoById(@PathVariable("id") int id) {
        Expense expense = this.expenseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Expense with %id not found", id)));
        return new ExpenseDto(expense);
    }

    @GetMapping("user/{id}/expenses")
    public List<ExpenseDto> getUserExpensesDto(@PathVariable("id") int userId){
        return this.expenseService.getExpensesByUserId(userId)
                .stream()
                .map(expense -> new ExpenseDto(expense))
                .collect(Collectors.toList());
    }

    @PostMapping("/user/{id}/expense")
    public ExpenseDto postExpense(@PathVariable("id") int userId, @Valid @RequestBody ExpenseDtoInput expenseDtoInput) {
        return this.expenseService.createExpenseFromDtoInput(userId, expenseDtoInput);
    }

    // @PostMapping("/user/{username}/expense")
    // public Expense postExpense(@PathVariable("username") String username, @Valid @RequestBody ExpenseDto expenseDto) {
    //     return this.expenseRepository.save(this.expenseService.createExpenseFromDto(expenseDto));
    // }
}
