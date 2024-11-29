package com.app.springBack.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.app.springBack.dto.ExpenseDto;
import com.app.springBack.dto.ExpenseDtoInput;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int expenseId;

    @NotNull(message = "Name cannot be null")
    private String expenseName;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal expensePrice;

    private String expenseNote; 

    private LocalDate expenseDate = LocalDate.now(ZoneId.of("Europe/Paris"));

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = true)
    private Address expenseAdress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Expense must be assigned to a Category")
    private Category expenseCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Expense must be assigned to a User")
    private User user;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    protected Expense() {}

    public Expense(String expenseName, BigDecimal expensePrice, String expenseNote, LocalDate expenseDate, Category expenseCategory, User user) {
        this.expenseName = expenseName;
        this.expensePrice = expensePrice;
        this.expenseNote = expenseNote;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
        this.user = user;
    }

    public Expense(ExpenseDto expenseDto, Category expenseCategory, User user) {
        this.expenseName = expenseDto.getExpenseName();
        this.expensePrice = expenseDto.getExpensePrice();
        this.expenseNote = expenseDto.getExpenseNote();
        this.expenseCategory = expenseCategory;
        this.user = user;
    }

    public Expense(ExpenseDtoInput expenseDtoInput, Category expenseCategory, User user) {
        this.expenseName = expenseDtoInput.getExpenseName();
        this.expensePrice = expenseDtoInput.getExpensePrice();
        this.expenseNote = expenseDtoInput.getExpenseNote();
        this.expenseDate = expenseDtoInput.getExpenseDate();
        this.expenseCategory = expenseCategory;
        this.user = user;
    }

    public Expense(ExpenseDtoInput expenseDtoInput, Address expenseAddress, Category expenseCategory, User user) {
        this.expenseName = expenseDtoInput.getExpenseName();
        this.expensePrice = expenseDtoInput.getExpensePrice();
        this.expenseNote = expenseDtoInput.getExpenseNote();
        this.expenseDate = expenseDtoInput.getExpenseDate();
        this.expenseAdress = expenseAddress;
        this.expenseCategory = expenseCategory;
        this.user = user;
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

    public int getId() {
        return this.expenseId;
    }

    public String getExpenseName() {
        return this.expenseName;
    }

    public BigDecimal getExpensePrice() {
        return this.expensePrice;
    }

    public String getExpenseNote() {
        return this.expenseNote;
    }

    public LocalDate getExpenseDate() {
        return this.expenseDate;
    }

    public User getUser() {
        return this.user;
    }

    public Address getExpenseAdress() {
        return this.expenseAdress;
    }

    public Category getExpenseCategory() {
        return this.expenseCategory;
    }

    public ZonedDateTime getUpdateDate() {
        return this.updatedAt;
    }

    public ZonedDateTime getCreationDate() {
        return this.createdAt;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public void setExpensePrice(BigDecimal expensePrice) {
        this.expensePrice = expensePrice;
    }

    public void setExpenseNote(String expenseNote) {
        this.expenseNote = expenseNote;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpenseCategory(Category expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setExpenseAdress(Address expenseAdress) {
        this.expenseAdress = expenseAdress;
    }
}
