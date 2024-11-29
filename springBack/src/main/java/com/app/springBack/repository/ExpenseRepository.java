package com.app.springBack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springBack.model.Expense;


public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    Optional<Expense> findByExpenseName(String expenseName);

    List<Expense> findByUserId(int userId);
}
