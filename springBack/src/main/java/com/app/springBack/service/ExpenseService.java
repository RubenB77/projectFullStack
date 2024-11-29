package com.app.springBack.service;

import com.app.springBack.model.Expense;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.springBack.dto.ExpenseDto;
import com.app.springBack.dto.ExpenseDtoInput;

@Service
public interface ExpenseService {
    public Expense createExpenseFromDto(ExpenseDto expenseDto);
    public ExpenseDto createExpenseFromDtoInput(int userId, ExpenseDtoInput expenseDtoInput);
    public List<Expense> getExpensesByUserId(int userId);
}