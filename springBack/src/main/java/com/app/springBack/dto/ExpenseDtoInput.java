package com.app.springBack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.app.springBack.model.Expense;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ExpenseDtoInput {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int expenseId;

    @NotNull(message = "expenseName cannot be null")
    private String expenseName;

    @NotNull(message = "expensePrice cannot be null")
    @Positive(message = "expensePrice must be positive")
    private BigDecimal expensePrice;

    private String expenseNote;

    private LocalDate expenseDate = LocalDate.now(ZoneId.of("Europe/Paris"));

    private AddressDto expenseAddress;

    @NotNull(message = "Expense must be assigned to a Category")
    private int categoryId;

    private ZonedDateTime createdAt;

    public ExpenseDtoInput(String expenseName, BigDecimal expensePrice, String expenseNote, LocalDate expenseDate, AddressDto expenseAddress, int categoryId, ZonedDateTime createdAt) {
        this.expenseName = expenseName;
        this.expensePrice = expensePrice;
        this.expenseNote = expenseNote;
        this.expenseDate = expenseDate;
        this.expenseAddress = expenseAddress;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public ExpenseDtoInput(Expense expense) {
        this.expenseName = expense.getExpenseName();
        this.expensePrice = expense.getExpensePrice();
        this.expenseNote = expense.getExpenseNote();
        this.expenseDate = expense.getExpenseDate();
        this.categoryId = expense.getExpenseCategory().getCategoryId();
        this.createdAt = expense.getCreationDate();
    }

    public String getExpenseName() {
        return this.expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public BigDecimal getExpensePrice() {
        return this.expensePrice;
    }

    public void setExpensePrice(BigDecimal expensePrice) {
        this.expensePrice = expensePrice;
    }

    public String getExpenseNote() {
        return this.expenseNote;
    }

    public void setExpenseNote(String expenseNote) {
        this.expenseNote = expenseNote;
    }

    public AddressDto getExpenseAddress() {
        return this.expenseAddress;
    }

    public void setExpenseAddress(AddressDto expenseAddress) {
        this.expenseAddress = expenseAddress;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getExpenseDate() {
        return this.expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
}
